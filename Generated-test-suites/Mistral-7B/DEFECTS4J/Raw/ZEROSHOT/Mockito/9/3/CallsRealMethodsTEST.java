package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

 private CallsRealMethods callsRealMethods;
 private SomeClass someClass;

 @Before
 public void setUp() {
 callsRealMethods = new CallsRealMethods();
 someClass = Mockito.spy(new SomeClass());
 Mockito.doAnswer(callsRealMethods).when(someClass).methodToBeTested();
 }

 @Test
 public void testCallRealMethod() {
 when(someClass.otherMethod()).thenReturn("expectedResult");

 someClass.methodToBeTested();

 verify(someClass).methodToBeTested(); // verify that method was called

 // test that real method was called and returned expected result
 verify(someClass).otherMethod().wasCalledWith(any());
 verify(someClass).otherMethod().thenReturn("expectedResult");

 assertEquals("expectedResult", someClass.methodToBeTested());
 }

 @Test(expected = Throwable.class)
 public void testCallRealMethodWithException() {
 when(someClass.otherMethod()).thenThrow(new RuntimeException("expectedException"));

 try {
 someClass.methodToBeTested();
 } catch (RuntimeException e) {
 assertEquals("expectedException", e.getMessage());
 }
 }

 @Test
 public void testSerializability() {
 // test serializability
 callsRealMethods = new CallsRealMethods();
 someClass = Mockito.spy(new SomeClass());
 Mockito.doAnswer(callsRealMethods).when(someClass).methodToBeTested();

 byte[] serialized = SerializationUtil.serialize(callsRealMethods);
 CallsRealMethods deserialized = (CallsRealMethods) SerializationUtil.deserialize(serialized);

 Mockito.doAnswer(deserialized).when(someClass).methodToBeTested();

 someClass.methodToBeTested();

 verify(someClass).methodToBeTested();
 }
}

public class SomeClass {
 private String otherResult;

 public String otherMethod() {
 return otherResult;
 }

 public void methodToBeTested() {
 otherResult = "result";
 // some complex logic here
 }
}
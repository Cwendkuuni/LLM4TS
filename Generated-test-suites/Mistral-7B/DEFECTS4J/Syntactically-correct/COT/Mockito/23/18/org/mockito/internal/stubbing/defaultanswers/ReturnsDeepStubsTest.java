import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private static class TestClass {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Number> mock1;
 GenericsNest<Number> mock2;
 GenericsNest<Number> mock3;

 void testDeepStubs() {
 GenericsNest<Number> deepStub = ReturnsDeepStubsTest.getDeepStub(GenericsNest.class);
 mock1 = deepStub;

 Number number = deepStub.entrySet().iterator().next().getValue().iterator().next();
 mock2 = number.iterator().next();

 number = mock2.iterator().next();
 mock3 = number;

 assertEquals(mock3, mock1);
 }
 }

 @Test
 public void testAnswer() throws Exception {
 // Test the answer method with a simple non-generic type
 Object answer = new ReturnsDeepStubs().answer(Mockito.mock(Number.class));
 assertNotNull(answer);

 // Test the answer method with a generic type
 Type returnType = TestClass.class.getDeclaredMethod("testDeepStubs").getGenericReturnType();
 GenericDeclaration genericDeclaration = returnType.getDeclaringClass();
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(genericDeclaration);
 Object deepStub = new ReturnsDeepStubs().answer(mock(TestClass.class, new ReturnsDeepStubs()));
 assertNotNull(deepStub);

 // Test that the correct mock is returned when the invocation matches
 TestClass testClass = (TestClass) deepStub;
 Number number = testClass.mock1.entrySet().iterator().next().getValue().iterator().next();
 Number number2 = testClass.mock2.iterator().next();
 Number number3 = testClass.mock3;
 assertEquals(number3, number2);
 assertEquals(number3, number);

 // Test that the correct mock is returned when the invocation matches a nested generic type
 Number mockNumber = testClass.mock3;
 Number deepStubNumber = testClass.mock1.entrySet().iterator().next().getValue().iterator().next();
 Number mockNumber2 = deepStubNumber.iterator().next();
 Number mockNumber3 = mockNumber;
 assertEquals(mockNumber3, mockNumber2);
 assertEquals(mockNumber3, mockNumber);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedOperationException() throws Exception {
 // Test that the answer method returns an empty value for unsupported types
 Object answer = new ReturnsDeepStubs().answer(Mockito.mock(List.class));
 assertEquals(Collections.emptyList(), answer);
 }

 @Test
 public void testGetMock() throws Exception {
 // Test the getMock method with a simple non-generic type
 Object mock = Mockito.mock(Number.class);
 Object deepStub = new ReturnsDeepStubs().getMock(new InvocationOnMock(mock, Number.class), new GenericMetadataSupport(Number.class.getTypeName()));
 assertEquals(mock, deepStub);

 // Test the getMock method with a generic type
 InvocationOnMock invocation = Mockito.mock(TestClass.class);
 Object deepStub2 = new ReturnsDeepStubs().getMock(invocation, new GenericMetadataSupport(TestClass.class.getTypeName()));
 assertNotNull(deepStub2);

 // Test that the correct mock is returned when the invocation matches
 TestClass testClass = (TestClass) deepStub2;
 Number number = testClass.mock1.entrySet().iterator().next().getValue().iterator().next();
 Number number2 = testClass.mock2.iterator().next();
 Number number3 = testClass.mock3;
 assertEquals(number3, number2);
 assertEquals(number3, number);
 }

 @Test
 public void testCreateNewDeepStubMock() throws Exception {
 // Test the createNewDeepStubMock method with a simple non-generic type
 Object mock = Mockito.mock(Number.class);
 Object deepStub = new ReturnsDeepStubs().createNewDeepStubMock(new GenericMetadataSupport(Number.class.getTypeName()));
 assertEquals(mock, deepStub);

 // Test the createNewDeepStubMock method with a generic type
 Type returnType = TestClass.class.getDeclaredMethod("testDeepStubs").getGenericReturnType();
 GenericDeclaration genericDeclaration = returnType.getDeclaringClass();
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(genericDeclaration);
 Object deepStub3 = new ReturnsDeepStubs().createNewDeepStubMock(returnTypeGenericMetadata);
 assertNotNull(deepStub3);
 }

 @Test
 public void testWithSettingsUsing() throws Exception {
 // Test the withSettingsUsing method with a simple non-generic type
 Object mock = Mockito.mock(Number.class);
 Object deepStub = new ReturnsDeepStubs().withSettingsUsing(new GenericMetadataSupport(Number.class.getTypeName())).createNewDeepStubMock(new GenericMetadataSupport(Number.class.getTypeName()));
 assertEquals(mock, deepStub);

 // Test the withSettingsUsing method with a generic type
 Type returnType = TestClass.class.getDeclaredMethod("testDeepStubs").getGenericReturnType();
 GenericDeclaration genericDeclaration = returnType.getDeclaringClass();
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(genericDeclaration);
 Object deepStub4 = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata).createNewDeepStubMock(returnTypeGenericMetadata);
 assertNotNull(deepStub4);
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 // Test the recordDeepStubMock method with a simple non-generic type
 Object mock = Mockito.mock(Number.class);
 Object container = Mockito.mock(InvocationContainerImpl.class);
 Object deepStub = new ReturnsDeepStubs().recordDeepStubMock(mock, container);
 assertEquals(mock, deepStub);

 // Test the recordDeepStubMock method with a generic type
 Object testClassMock = Mockito.mock(TestClass.class);
 Object invocationContainer = Mockito.mock(InvocationContainerImpl.class);
 Object deepStub5 = new ReturnsDeepStubs().recordDeepStubMock(testClassMock, invocationContainer);
 assertNotNull(deepStub5);

 // Test that the answer method returns the recorded mock when the invocation matches
 TestClass testClass = (TestClass) deepStub5;
 Number number = testClass.mock1.entrySet().iterator().next().getValue().iterator().next();
 Number number2 = testClass.mock2.iterator().next();
 Number number3 = testClass.mock3;
 assertEquals(number3, number2);
 assertEquals(number3, number);
 }

 @Test
 public void testActualParameterizedType() throws Exception {
 // Test the actualParameterizedType method with a simple non-generic type
 Object mock = Mockito.mock(Number.class);
 Object genericMetadataSupport = new GenericMetadataSupport(Number.class.getTypeName());
 Object actualParameterizedType = new ReturnsDeepStubs().actualParameterizedType(mock);
 assertEquals(genericMetadataSupport, actualParameterizedType);

 // Test the actualParameterizedType method with a generic type
 Type returnType = TestClass.class.getDeclaredMethod("testDeepStubs").getGenericReturnType();
 GenericDeclaration genericDeclaration = returnType.getDeclaringClass();
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(genericDeclaration);
 Object actualParameterizedType2 = new ReturnsDeepStubs().actualParameterizedType(Mockito.mock(TestClass.class, new ReturnsDeepStubs()));
 assertEquals(returnTypeGenericMetadata, actualParameterizedType2);
 }

 private static Object getDeepStub(Class<?> clazz) throws Exception {
 Object mock = Mockito.mock(clazz);
 Object returnsDeepStubs = mock(ReturnsDeepStubs.class);
 Mockito.doAnswer(returnsDeepStubs).when(mock).answer(any(InvocationOnMock.class));
 return mock;
 }
}
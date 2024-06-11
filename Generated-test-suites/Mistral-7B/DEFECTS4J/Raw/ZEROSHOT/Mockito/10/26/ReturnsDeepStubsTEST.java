package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.util.reflection.GenericMetadataSupport.inferFrom;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs deepStubs;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private Object mock4;

 @Before
 public void setUp() throws Exception {
 deepStubs = new ReturnsDeepStubs();

 mock1 = mock(Map.class, withSettings().defaultAnswer(deepStubs));
 mock2 = mock(Set.class, withSettings().defaultAnswer(deepStubs));
 mock3 = mock(Number.class, withSettings().defaultAnswer(deepStubs));
 mock4 = mock(GenericsNest.class, withSettings().defaultAnswer(deepStubs));
 }

 @Test
 public void testDeepStubs() throws Exception {
 // test deep stubs for Map
 Map<Number, Set<Number>> map = (Map<Number, Set<Number>>) mock1;
 Set<Number> set = mock2;
 Number number = mock3;
 Set<Number> setFromMap = map.entrySet().iterator().next().getValue().iterator().next();
 verify(setFromMap).contains(number);

 // test deep stubs for Set
 Set<Number> setFromMap2 = (Set<Number>) set.iterator().next();
 verify(setFromMap2).contains(number);

 // test deep stubs for GenericsNest
 GenericsNest<Number> genericsNest = (GenericsNest<Number>) mock4;
 Number numberFromGenericsNest = genericsNest.entrySet().iterator().next().getValue().iterator().next();
 verify(numberFromGenericsNest).equals(number);

 // test no stubs for non-mockable types
 Object nonMockableType = new Object() {};
 verifyZeroInteractions(nonMockableType);
 }

 @Test
 public void testDeepStubsSerialization() throws Exception {
 Object deepStub = mock(Map.class, withSettings().defaultAnswer(deepStubs));
 Object deepStubSerialized = Mockito.deserialize( Mockito.serializedValue(deepStub), Map.class );

 assertSame(deepStub, deepStubSerialized);
 }
}
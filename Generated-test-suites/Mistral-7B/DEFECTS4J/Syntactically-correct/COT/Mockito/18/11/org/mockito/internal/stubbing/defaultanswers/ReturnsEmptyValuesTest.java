import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testToStringMethod() {
 InvocationOnMock invocation = Mockito.mock(Object.class);
 Mockito.when(invocation.toString()).thenAnswer(returnsEmptyValues);
 String result = invocation.toString();
 MockName mockName = Mockito.mock(MockName.class);
 Mockito.when(mockName.isDefault()).thenReturn(true);
 Mockito.when(mockName.toString()).thenReturn("MockName");
 String expected = "Mock for Object, hashCode: " + invocation.hashCode();
 assertEquals(expected, result);
 }

 @Test
 public void testCompareToMethod() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation1.getMock()).thenReturn(mock1);
 Mockito.when(invocation1.getArguments()).thenReturn(new Object[0]);
 Mockito.when(invocation1.getMethod()).thenReturn(Mockito.method(Object.class, "compareTo", Object.class));
 Mockito.when(invocation2.getMock()).thenReturn(mock2);
 Mockito.when(invocation2.getArguments()).thenReturn(new Object[] { mock1 });
 Mockito.when(invocation2.getMethod()).thenReturn(Mockito.method(Object.class, "compareTo", Object.class));
 Mockito.when(mock1.equals(mock2)).thenReturn(true);
 int result1 = returnsEmptyValues.answer(invocation1);
 int result2 = returnsEmptyValues.answer(invocation2);
 assertEquals(0, result1);
 assertEquals(0, result2);
 }

 @Test
 public void testPrimitiveTypes() {
 Class<?> intType = int.class;
 Class<?> booleanType = boolean.class;
 Class<?> doubleType = double.class;
 Class<?> charType = char.class;
 Class<?> longType = long.class;
 Class<?> floatType = float.class;
 Class<?> byteType = byte.class;
 Class<?> shortType = short.class;
 InvocationOnMock intInvocation = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock booleanInvocation = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock doubleInvocation = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock charInvocation = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock longInvocation = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock floatInvocation = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock byteInvocation = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock shortInvocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(intInvocation.getMethod()).thenReturn(Mockito.method(intType, "toString", new Class[0]));
 Mockito.when(booleanInvocation.getMethod()).thenReturn(Mockito.method(booleanType, "equals", new Class[] { booleanType }));
 Mockito.when(doubleInvocation.getMethod()).thenReturn(Mockito.method(doubleType, "compareTo", doubleType));
 Mockito.when(charInvocation.getMethod()).thenReturn(Mockito.method(charType, "compareTo", charType));
 Mockito.when(longInvocation.getMethod()).thenReturn(Mockito.method(longType, "compareTo", longType));
 Mockito.when(floatInvocation.getMethod()).thenReturn(Mockito.method(floatType, "compareTo", floatType));
 Mockito.when(byteInvocation.getMethod()).thenReturn(Mockito.method(byteType, "compareTo", byteType));
 Mockito.when(shortInvocation.getMethod()).thenReturn(Mockito.method(shortType, "compareTo", shortType));
 Object resultInt = returnsEmptyValues.answer(intInvocation);
 Object resultBoolean = returnsEmptyValues.answer(booleanInvocation);
 Object resultDouble = returnsEmptyValues.answer(doubleInvocation);
 Object resultChar = returnsEmptyValues.answer(charInvocation);
 Object resultLong = returnsEmptyValues.answer(longInvocation);
 Object resultFloat = returnsEmptyValues.answer(floatInvocation);
 Object resultByte = returnsEmptyValues.answer(byteInvocation);
 Object resultShort = returnsEmptyValues.answer(shortInvocation);
 assertNotNull(resultInt);
 assertNotNull(resultBoolean);
 assertNotNull(resultDouble);
 assertNotNull(resultChar);
 assertNotNull(resultLong);
 assertNotNull(resultFloat);
 assertNotNull(resultByte);
 assertNotNull(resultShort);
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testUnknownType() {
 Class<?> unknownType = String[].class;
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(Mockito.method(unknownType, "toString", new Class[0]));
 try {
 returnsEmptyValues.answer(invocation);
 } catch (IllegalArgumentException e) {
 assertEquals("Unable to provide empty value for an unknown type: " + unknownType, e.getMessage());
 }
 }
}
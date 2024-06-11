package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.util.Arrays.asList;
import static org.mockito.internal.util.Collections.emptyList;
import static org.mockito.internal.util.Collections.singletonList;
import static org.mockito.internal.util.reflection.FieldAccessUtils.getFieldValue;
import static org.mockito.internal.util.reflection.FieldAccessUtils.setFieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockName;

public class ReturnsEmptyValuesTest {

private ReturnsEmptyValues returnsEmptyValues;
private ObjectMethodsGuru methodsGuru;
private MockUtil mockUtil;

@Before
public void setUp() {
methodsGuru = new ObjectMethodsGuru();
mockUtil = new MockUtil();
returnsEmptyValues = new ReturnsEmptyValues();
}

@Test
public void testAnswer_toStringMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("toString", new Object()[0]);
MockName mockName = mockUtil.getMockName(new Object());
assertEquals(
"Mock for " + mockUtil.getMockSettings(new Object()).getTypeToMock().getSimpleName() + ", hashCode: " + new Object().hashCode(),
returnsEmptyValues.answer(invocation)
);
}

@Test
public void testAnswer_compareToMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("compareTo", new Integer(0), new Integer(1));
assertEquals(1, returnsEmptyValues.answer(invocation).intValue());
}

@Test
public void testAnswer_booleanReturningMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("booleanMethod", new Object()[0]);
assertNull(returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_byteReturningMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("byteMethod", new Object()[0]);
assertEquals((byte) 0, returnsEmptyValues.answer(invocation).byteValue());
}

@Test
public void testAnswer_shortReturningMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("shortMethod", new Object()[0]);
assertEquals((short) 0, returnsEmptyValues.answer(invocation).shortValue());
}

@Test
public void testAnswer_intReturningMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("intMethod", new Object()[0]);
assertEquals(0, returnsEmptyValues.answer(invocation).intValue());
}

@Test
public void testAnswer_longReturningMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("longMethod", new Object()[0]);
assertEquals(0L, returnsEmptyValues.answer(invocation).longValue());
}

@Test
public void testAnswer_charReturningMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("charMethod", new Object()[0]);
assertEquals((char) 0, returnsEmptyValues.answer(invocation).charValue());
}

@Test
public void testAnswer_floatReturningMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("floatMethod", new Object()[0]);
assertEquals(0.0F, returnsEmptyValues.answer(invocation).floatValue(), 0.0F);
}

@Test
public void testAnswer_doubleReturningMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("doubleMethod", new Object()[0]);
assertEquals(0.0D, returnsEmptyValues.answer(invocation).doubleValue(), 0.0D);
}

@Test
public void testAnswer_ObjectReturningMethod() {
Invocation invocation = MockitoTestUtils.createMockInvocation("objectMethod", new Object()[0]);
assertNull(returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_ArrayList() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
assertEquals(emptyList(), returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_LinkedList() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_ArrayListWithElements() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
setFieldValue(invocation, "arguments", singletonList(new Integer(1)).toArray());
assertEquals(singletonList(new Integer(1)), returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_LinkedHashSet() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_TreeSet() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_LinkedHashSetWithElements() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
setFieldValue(invocation, "arguments", asList(new Integer(1), new Integer(2)).toArray());
assertEquals(new LinkedHashSet<Object>(asList(new Integer(1), new Integer(2))), returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_HashMap() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_TreeMap() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_LinkedHashMap() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_LinkedHashMapWithKeyValue() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
Map<Object, Object> map = new LinkedHashMap<Object, Object>();
map.put(new Integer(1), new Integer(1));
setFieldValue(invocation, "arguments", map.entrySet().toArray());
assertEquals(map, returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_ArrayListWithCustomClass() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
MyClass myClass = new MyClass();
setFieldValue(invocation, "arguments", singletonList(myClass).toArray());
assertEquals(singletonList(myClass), returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_LinkedListWithCustomClass() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
MyClass myClass = new MyClass();
setFieldValue(invocation, "arguments", new LinkedList<MyClass>() {{ add(myClass); }});
assertEquals(new LinkedList<MyClass>() {{ add(myClass); }}, returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_LinkedHashSetWithCustomClass() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
MyClass myClass = new MyClass();
setFieldValue(invocation, "arguments", new LinkedHashSet<MyClass>() {{ add(myClass); }});
assertEquals(new LinkedHashSet<MyClass>() {{ add(myClass); }}, returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_HashMapWithCustomClassKey() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
MyKey myKey = new MyKey();
MyClass myClass = new MyClass();
Map<MyKey, MyClass> map = new LinkedHashMap<MyKey, MyClass>() {{ put(myKey, myClass); }};
setFieldValue(invocation, "arguments", map.entrySet().toArray());
assertEquals(map, returnsEmptyValues.answer(invocation));
}

@Test
public void testAnswer_CollectionReturningMethod_TreeMapWithCustomClassKey() {
Invocation invocation = MockitoTestUtils.createMockInvocation("collectionMethod", new Object()[0]);
MyKey myKey = new MyKey();
MyClass myClass = new MyClass();
Map<MyKey, MyClass> map = new TreeMap<MyKey, MyClass>() {{ put(myKey, myClass); }};
setFieldValue(invocation, "arguments", map.entrySet().toArray());
assertEquals(map, returnsEmptyValues.answer(invocation));
}

private static Invocation createMockInvocation(String methodName, Object... args) {
return new InvocationOnMock() {{
setTarget(new Object());
setMethod(MockitoTestUtils.getMethod(ReturnsEmptyValues.class, methodName, args));
setArguments(args);
}};
}

private static List<Object> asList(Object... objects) {
return new ArrayList<Object>(asList(objects));
}
}

class MyClass {

}

class MyKey {

}

class MockitoTestUtils {

public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
try {
return clazz.getMethod(name, parameterTypes);
} catch (NoSuchMethodException e) {
throw new RuntimeException("Could not find method [" + name + "] on class [" + clazz.getName() + "] with parameters: " + Arrays.toString(parameterTypes), e);
}
}
}
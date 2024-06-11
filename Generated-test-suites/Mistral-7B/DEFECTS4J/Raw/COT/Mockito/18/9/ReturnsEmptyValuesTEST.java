package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private Object mock;

 @Before
 public void setup() {
 mock = mock(Object.class);
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswerToStringMethod() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.setMethod(MockitoTestUtils.getMethods().get("toString"));
 invocation.setMock(mock);

 String result = (String) returnsEmptyValues.answer(invocation);

 verify(mock).toString();
 assertThat(result).isInstanceOf(String.class);
 assertThat(result).contains("Mock for [Class name], hashCode: ");
 }

 @Test
 public void testAnswerCompareToMethod() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.setMethod(MockitoTestUtils.getMethods().get("compareTo"));
 invocation.setArguments(new Object[]{mock});
 invocation.setMock(mock);

 int result = (int) returnsEmptyValues.answer(invocation);

 verify(mock).compareTo((Object) mock);
 assertThat(result).isEqualTo(0);
 }

 @Test(expected = AssertionError.class)
 public void testAnswerCompareToMethodWithDifferentObjects() {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.setMethod(MockitoTestUtils.getMethods().get("compareTo"));
 invocation.setArguments(new Object[]{new Object(), mock});
 invocation.setMock(mock);

 int result = (int) returnsEmptyValues.answer(invocation);

 fail("Expected exception: java.lang.AssertionError");
 }

 @Test
 public void testReturnValueForPrimitiveBoolean() {
 Class<Boolean> booleanType = Boolean.TYPE;
 Object result = returnsEmptyValues.returnValueFor(booleanType);

 assertThat(result).isInstanceOf(Boolean.class);
 assertThat(result).isEqualTo(Boolean.FALSE);
 }

 @Test
 public void testReturnValueForPrimitiveByte() {
 Class<Byte> byteType = Byte.TYPE;
 Object result = returnsEmptyValues.returnValueFor(byteType);

 assertThat(result).isInstanceOf(Byte.class);
 assertThat(result).isEqualTo((byte) 0);
 }

 @Test
 public void testReturnValueForPrimitiveChar() {
 Class<Character> charType = Character.TYPE;
 Object result = returnsEmptyValues.returnValueFor(charType);

 assertThat(result).isInstanceOf(Character.class);
 assertThat(result).isEqualTo((char) 0);
 }

 @Test
 public void testReturnValueForPrimitiveShort() {
 Class<Short> shortType = Short.TYPE;
 Object result = returnsEmptyValues.returnValueFor(shortType);

 assertThat(result).isInstanceOf(Short.class);
 assertThat(result).isEqualTo((short) 0);
 }

 @Test
 public void testReturnValueForPrimitiveInt() {
 Class<Integer> intType = Integer.TYPE;
 Object result = returnsEmptyValues.returnValueFor(intType);

 assertThat(result).isInstanceOf(Integer.class);
 assertThat(result).isEqualTo(0);
 }

 @Test
 public void testReturnValueForPrimitiveLong() {
 Class<Long> longType = Long.TYPE;
 Object result = returnsEmptyValues.returnValueFor(longType);

 assertThat(result).isInstanceOf(Long.class);
 assertThat(result).isEqualTo(0L);
 }

 @Test
 public void testReturnValueForPrimitiveFloat() {
 Class<Float> floatType = Float.TYPE;
 Object result = returnsEmptyValues.returnValueFor(floatType);

 assertThat(result).isInstanceOf(Float.class);
 assertThat(result).isEqualTo((float) 0.0F);
 }

 @Test
 public void testReturnValueForPrimitiveDouble() {
 Class<Double> doubleType = Double.TYPE;
 Object result = returnsEmptyValues.returnValueFor(doubleType);

 assertThat(result).isInstanceOf(Double.class);
 assertThat(result).isEqualTo(0.0);
 }

 @Test
 public void testReturnValueForPrimitiveObject() {
 Class<Object> objectType = Object.class;
 Object result = returnsEmptyValues.returnValueFor(objectType);

 assertThat(result).isInstanceOf(Object.class);
 assertThat(result).isNull();
 }

 @Test
 public void testReturnValueForArrayOfBoolean() {
 Class<?> booleanArrayType = new ArrayTypeReference(Boolean[].class).getType();
 Object result = returnsEmptyValues.returnValueFor(booleanArrayType);

 assertThat(result).isInstanceOf(Boolean[].class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForArrayOfByte() {
 Class<?> byteArrayType = new ArrayTypeReference(Byte[].class).getType();
 Object result = returnsEmptyValues.returnValueFor(byteArrayType);

 assertThat(result).isInstanceOf(Byte[].class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForArrayOfChar() {
 Class<?> charArrayType = new ArrayTypeReference(Character[].class).getType();
 Object result = returnsEmptyValues.returnValueFor(charArrayType);

 assertThat(result).isInstanceOf(Character[].class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForArrayOfShort() {
 Class<?> shortArrayType = new ArrayTypeReference(Short[].class).getType();
 Object result = returnsEmptyValues.returnValueFor(shortArrayType);

 assertThat(result).isInstanceOf(Short[].class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForArrayOfInt() {
 Class<?> intArrayType = new ArrayTypeReference(Integer[].class).getType();
 Object result = returnsEmptyValues.returnValueFor(intArrayType);

 assertThat(result).isInstanceOf(Integer[].class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForArrayOfLong() {
 Class<?> longArrayType = new ArrayTypeReference(Long[].class).getType();
 Object result = returnsEmptyValues.returnValueFor(longArrayType);

 assertThat(result).isInstanceOf(Long[].class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForArrayOfFloat() {
 Class<?> floatArrayType = new ArrayTypeReference(Float[].class).getType();
 Object result = returnsEmptyValues.returnValueFor(floatArrayType);

 assertThat(result).isInstanceOf(Float[].class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForArrayOfDouble() {
 Class<?> doubleArrayType = new ArrayTypeReference(Double[].class).getType();
 Object result = returnsEmptyValues.returnValueFor(doubleArrayType);

 assertThat(result).isInstanceOf(Double[].class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForArrayOfObject() {
 Class<?> objectArrayType = new ArrayTypeReference(Object[].class).getType();
 Object result = returnsEmptyValues.returnValueFor(objectArrayType);

 assertThat(result).isInstanceOf(Object[].class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForCollectionObject() {
 Class<Collection<Object>> collectionObjectType = Collection.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(collectionObjectType);

 assertThat(result).isInstanceOf(Collection.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForSetObject() {
 Class<Set<Object>> setObjectType = Set.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(setObjectType);

 assertThat(result).isInstanceOf(Set.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForHashSetObject() {
 Class<HashSet<Object>> hashSetObjectType = HashSet.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(hashSetObjectType);

 assertThat(result).isInstanceOf(HashSet.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForTreeSetObject() {
 Class<TreeSet<Object>> treeSetObjectType = TreeSet.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(treeSetObjectType);

 assertThat(result).isInstanceOf(TreeSet.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForLinkedHashSetObject() {
 Class<LinkedHashSet<Object>> linkedHashSetObjectType = LinkedHashSet.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(linkedHashSetObjectType);

 assertThat(result).isInstanceOf(LinkedHashSet.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForListObject() {
 Class<List<Object>> listObjectType = List.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(listObjectType);

 assertThat(result).isInstanceOf(List.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForLinkedListObject() {
 Class<LinkedList<Object>> linkedListObjectType = LinkedList.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(linkedListObjectType);

 assertThat(result).isInstanceOf(LinkedList.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForArrayListObject() {
 Class<ArrayList<Object>> arrayListObjectType = ArrayList.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(arrayListObjectType);

 assertThat(result).isInstanceOf(ArrayList.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForMapObjectKeyObjectValue() {
 Class<Map<Object, Object>> mapObjectObjectType = Map.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(mapObjectObjectType);

 assertThat(result).isInstanceOf(Map.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForHashMapObjectKeyObjectValue() {
 Class<HashMap<Object, Object>> hashMapObjectObjectType = HashMap.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(hashMapObjectObjectType);

 assertThat(result).isInstanceOf(HashMap.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForTreeMapObjectKeyObjectValue() {
 Class<TreeMap<Object, Object>> treeMapObjectObjectType = TreeMap.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(treeMapObjectObjectType);

 assertThat(result).isInstanceOf(TreeMap.class);
 assertThat(result).isEmpty();
 }

 @Test
 public void testReturnValueForLinkedHashMapObjectKeyObjectValue() {
 Class<LinkedHashMap<Object, Object>> linkedHashMapObjectObjectType = LinkedHashMap.class.getGenericType();
 Object result = returnsEmptyValues.returnValueFor(linkedHashMapObjectObjectType);

 assertThat(result).isInstanceOf(LinkedHashMap.class);
 assertThat(result).isEmpty();
 }
}
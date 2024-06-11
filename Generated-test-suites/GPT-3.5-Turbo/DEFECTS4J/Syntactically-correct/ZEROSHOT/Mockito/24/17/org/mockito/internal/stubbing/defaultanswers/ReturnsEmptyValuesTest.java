import org.junit.Test;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswer_forToString_returnsDescriptionOfMock() {
        String result = (String) returnsEmptyValues.answer(createInvocation("toString"));

        assertEquals("Mock for Object, hashCode: 0", result);
    }

    @Test
    public void testAnswer_forCompareTo_returnsNonZero() {
        int result = (int) returnsEmptyValues.answer(createInvocation("compareTo"));

        assertNotEquals(0, result);
    }

    @Test
    public void testAnswer_forPrimitiveReturnType_returnsDefaultValue() {
        byte byteResult = (byte) returnsEmptyValues.answer(createInvocation("byteMethod"));
        short shortResult = (short) returnsEmptyValues.answer(createInvocation("shortMethod"));
        int intResult = (int) returnsEmptyValues.answer(createInvocation("intMethod"));
        long longResult = (long) returnsEmptyValues.answer(createInvocation("longMethod"));
        float floatResult = (float) returnsEmptyValues.answer(createInvocation("floatMethod"));
        double doubleResult = (double) returnsEmptyValues.answer(createInvocation("doubleMethod"));
        boolean booleanResult = (boolean) returnsEmptyValues.answer(createInvocation("booleanMethod"));
        char charResult = (char) returnsEmptyValues.answer(createInvocation("charMethod"));

        assertEquals(0, byteResult);
        assertEquals(0, shortResult);
        assertEquals(0, intResult);
        assertEquals(0L, longResult);
        assertEquals(0.0F, floatResult, 0);
        assertEquals(0.0, doubleResult, 0);
        assertEquals(false, booleanResult);
        assertEquals('\u0000', charResult);
    }

    @Test
    public void testAnswer_forPrimitiveWrapperReturnType_returnsDefaultValue() {
        Byte byteResult = (Byte) returnsEmptyValues.answer(createInvocation("ByteMethod"));
        Short shortResult = (Short) returnsEmptyValues.answer(createInvocation("ShortMethod"));
        Integer intResult = (Integer) returnsEmptyValues.answer(createInvocation("IntegerMethod"));
        Long longResult = (Long) returnsEmptyValues.answer(createInvocation("LongMethod"));
        Float floatResult = (Float) returnsEmptyValues.answer(createInvocation("FloatMethod"));
        Double doubleResult = (Double) returnsEmptyValues.answer(createInvocation("DoubleMethod"));
        Boolean booleanResult = (Boolean) returnsEmptyValues.answer(createInvocation("BooleanMethod"));
        Character charResult = (Character) returnsEmptyValues.answer(createInvocation("CharMethod"));

        assertEquals(Byte.valueOf((byte) 0), byteResult);
        assertEquals(Short.valueOf((short) 0), shortResult);
        assertEquals(Integer.valueOf(0), intResult);
        assertEquals(Long.valueOf(0L), longResult);
        assertEquals(Float.valueOf(0.0F), floatResult);
        assertEquals(Double.valueOf(0.0), doubleResult);
        assertEquals(Boolean.FALSE, booleanResult);
        assertEquals(Character.valueOf('\u0000'), charResult);
    }

    @Test
    public void testAnswer_forCollectionReturnType_returnsEmptyLinkedList() {
        Object result = returnsEmptyValues.answer(createInvocation("collectionMethod"));

        assertTrue(result instanceof LinkedList);
        assertEquals(0, ((LinkedList) result).size());
    }

    @Test
    public void testAnswer_forSetReturnType_returnsEmptyHashSet() {
        Object result = returnsEmptyValues.answer(createInvocation("setMethod"));

        assertTrue(result instanceof HashSet);
        assertEquals(0, ((HashSet) result).size());
    }

    @Test
    public void testAnswer_forHashSetReturnType_returnsEmptyHashSet() {
        Object result = returnsEmptyValues.answer(createInvocation("hashSetMethod"));

        assertTrue(result instanceof HashSet);
        assertEquals(0, ((HashSet) result).size());
    }

    @Test
    public void testAnswer_forSortedSetReturnType_returnsEmptyTreeSet() {
        Object result = returnsEmptyValues.answer(createInvocation("sortedSetMethod"));

        assertTrue(result instanceof TreeSet);
        assertEquals(0, ((TreeSet) result).size());
    }

    @Test
    public void testAnswer_forTreeSetReturnType_returnsEmptyTreeSet() {
        Object result = returnsEmptyValues.answer(createInvocation("treeSetMethod"));

        assertTrue(result instanceof TreeSet);
        assertEquals(0, ((TreeSet) result).size());
    }

    @Test
    public void testAnswer_forLinkedHashSetReturnType_returnsEmptyLinkedHashSet() {
        Object result = returnsEmptyValues.answer(createInvocation("linkedHashSetMethod"));

        assertTrue(result instanceof LinkedHashSet);
        assertEquals(0, ((LinkedHashSet) result).size());
    }

    @Test
    public void testAnswer_forListReturnType_returnsEmptyLinkedList() {
        Object result = returnsEmptyValues.answer(createInvocation("listMethod"));

        assertTrue(result instanceof LinkedList);
        assertEquals(0, ((LinkedList) result).size());
    }

    @Test
    public void testAnswer_forLinkedListReturnType_returnsEmptyLinkedList() {
        Object result = returnsEmptyValues.answer(createInvocation("linkedListMethod"));

        assertTrue(result instanceof LinkedList);
        assertEquals(0, ((LinkedList) result).size());
    }

    @Test
    public void testAnswer_forArrayListReturnType_returnsEmptyArrayList() {
        Object result = returnsEmptyValues.answer(createInvocation("arrayListMethod"));

        assertTrue(result instanceof ArrayList);
        assertEquals(0, ((ArrayList) result).size());
    }

    @Test
    public void testAnswer_forMapReturnType_returnsEmptyHashMap() {
        Object result = returnsEmptyValues.answer(createInvocation("mapMethod"));

        assertTrue(result instanceof HashMap);
        assertEquals(0, ((HashMap) result).size());
    }

    @Test
    public void testAnswer_forHashMapReturnType_returnsEmptyHashMap() {
        Object result = returnsEmptyValues.answer(createInvocation("hashMapMethod"));

        assertTrue(result instanceof HashMap);
        assertEquals(0, ((HashMap) result).size());
    }

    @Test
    public void testAnswer_forSortedMapReturnType_returnsEmptyTreeMap() {
        Object result = returnsEmptyValues.answer(createInvocation("sortedMapMethod"));

        assertTrue(result instanceof TreeMap);
        assertEquals(0, ((TreeMap) result).size());
    }

    @Test
    public void testAnswer_forTreeMapReturnType_returnsEmptyTreeMap() {
        Object result = returnsEmptyValues.answer(createInvocation("treeMapMethod"));

        assertTrue(result instanceof TreeMap);
        assertEquals(0, ((TreeMap) result).size());
    }

    @Test
    public void testAnswer_forLinkedHashMapReturnType_returnsEmptyLinkedHashMap() {
        Object result = returnsEmptyValues.answer(createInvocation("linkedHashMapMethod"));

        assertTrue(result instanceof LinkedHashMap);
        assertEquals(0, ((LinkedHashMap) result).size());
    }

    private InvocationOnMock createInvocation(String methodName) {
        try {
            return InvocationBuilder.builder()
                    .mock(Mockito.mock(Object.class))
                    .args(new Object[0])
                    .method(Object.class.getMethod(methodName))
                    .build();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
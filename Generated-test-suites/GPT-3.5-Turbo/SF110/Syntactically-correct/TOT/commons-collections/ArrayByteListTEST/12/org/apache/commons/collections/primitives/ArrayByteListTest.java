import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testArrayByteList_defaultConstructor_returnsInitializedObject() {
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testArrayByteList_initialCapacityConstructor_returnsInitializedObjectWithCorrectCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(10);
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
        byte[] data = arrayByteList.getData();
        assertNotNull(data);
        assertEquals(10, data.length);
    }

    @Test
    public void testArrayByteList_byteCollectionConstructor_returnsInitializedObjectWithCorrectValues() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        ArrayByteList arrayByteList = new ArrayByteList(byteCollection);
        assertNotNull(arrayByteList);
        assertEquals(2, arrayByteList.size());
        byte[] data = arrayByteList.getData();
        assertNotNull(data);
        assertEquals((byte) 1, data[0]);
        assertEquals((byte) 2, data[1]);
    }

    @Test
    public void testGet_validIndex_returnsCorrectValue() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_invalidIndex_throwsIndexOutOfBoundsException() {
        arrayByteList.get(0);
    }

    @Test
    public void testSize_emptyList_returnsZero() {
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testSize_nonEmptyList_returnsCorrectSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt_validIndex_returnsRemovedValueAndUpdatesSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        byte removedValue = arrayByteList.removeElementAt(0);
        assertEquals((byte) 1, removedValue);
        assertEquals(1, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_invalidIndex_throwsIndexOutOfBoundsException() {
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testSet_validIndex_updatesValueAndReturnsPreviousValue() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        byte previousValue = arrayByteList.set(1, (byte) 3);
        assertEquals((byte) 2, previousValue);
        assertEquals((byte) 3, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_invalidIndex_throwsIndexOutOfBoundsException() {
        arrayByteList.set(0, (byte) 1);
    }

    @Test
    public void testAdd_validIndex_addsElementAndIncreasesSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        arrayByteList.add(1, (byte) 3);
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 3, arrayByteList.get(1));
        assertEquals((byte) 2, arrayByteList.get(2));
        assertEquals(3, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_invalidIndex_throwsIndexOutOfBoundsException() {
        arrayByteList.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity_increasedCapacity_updatesDataArray() {
        byte[] initialData = arrayByteList.getData();
        arrayByteList.ensureCapacity(10);
        byte[] updatedData = arrayByteList.getData();

        assertEquals(10, updatedData.length);
        assertNotEquals(initialData, updatedData);
    }

    @Test
    public void testEnsureCapacity_decreasedCapacity_doesNotChangeDataArray() {
        arrayByteList.ensureCapacity(10);
        byte[] initialData = arrayByteList.getData();
        arrayByteList.ensureCapacity(5);
        byte[] updatedData = arrayByteList.getData();

        assertEquals(10, updatedData.length);
        assertEquals(initialData, updatedData);
    }

    @Test
    public void testTrimToSize_shrinksDataArrayToSize() {
        arrayByteList.ensureCapacity(10);
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.trimToSize();
        byte[] data = arrayByteList.getData();

        assertEquals(2, arrayByteList.size());
        assertEquals(2, data.length);
    }

    @Test
    public void testWriteObject_serializesDataArrayWithSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(arrayByteList);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] serializedData = byteArrayOutputStream.toByteArray();
        assertNotNull(serializedData);
    }

    @Test
    public void testReadObject_deserializesDataArrayWithSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(arrayByteList);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
            objectInputStream.close();

            assertNotNull(deserializedList);
            assertEquals(2, deserializedList.size());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange_invalidIndex_throwsIndexOutOfBoundsException() {
        arrayByteList.checkRange(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint_invalidIndex_throwsIndexOutOfBoundsException() {
        arrayByteList.checkRangeIncludingEndpoint(1);
    }

    @Test
    public void testArrayByteListToString_containsClassNameAndSize() {
        assertEquals("ArrayByteList[size=0]", arrayByteList.toString());

        arrayByteList.add((byte) 1);
        assertEquals("ArrayByteList[size=1]", arrayByteList.toString());
    }

    @Test
    public void testArrayByteListEquals_equalObjects_returnsTrue() {
        ArrayByteList arrayByteList1 = new ArrayByteList();
        ArrayByteList arrayByteList2 = new ArrayByteList();

        assertEquals(arrayByteList1, arrayByteList2);
    }

    @Test
    public void testArrayByteListEquals_differentObjectsWithSameValues_returnsTrue() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        ArrayByteList arrayByteList2 = new ArrayByteList();
        arrayByteList2.add((byte) 1);
        arrayByteList2.add((byte) 2);

        assertEquals(arrayByteList, arrayByteList2);
    }

    @Test
    public void testArrayByteListEquals_differentObjectsWithDifferentValues_returnsFalse() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        ArrayByteList arrayByteList2 = new ArrayByteList();
        arrayByteList2.add((byte) 2);
        arrayByteList2.add((byte) 3);

        assertNotEquals(arrayByteList, arrayByteList2);
    }

    @Test
    public void testArrayByteListEquals_differentObjectTypes_returnsFalse() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        assertNotEquals(arrayByteList, "notAnArrayByteList");
    }

    @Test
    public void testHashCode_equalObjects_haveSameHashCode() {
        ArrayByteList arrayByteList1 = new ArrayByteList();
        ArrayByteList arrayByteList2 = new ArrayByteList();

        assertEquals(arrayByteList1.hashCode(), arrayByteList2.hashCode());
    }

    @Test
    public void testHashCode_differentObjectsWithSameValues_haveSameHashCode() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        ArrayByteList arrayByteList2 = new ArrayByteList();
        arrayByteList2.add((byte) 1);
        arrayByteList2.add((byte) 2);

        assertEquals(arrayByteList.hashCode(), arrayByteList2.hashCode());
    }

    @Test
    public void testHashCode_differentObjectsWithDifferentValues_haveDifferentHashCodes() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        ArrayByteList arrayByteList2 = new ArrayByteList();
        arrayByteList2.add((byte) 2);
        arrayByteList2.add((byte) 3);

        assertNotEquals(arrayByteList.hashCode(), arrayByteList2.hashCode());
    }

    @Test
    public void testHashCode_equalAndEqualObjects_haveSameHashCode() {
        ArrayByteList arrayByteList1 = new ArrayByteList();
        arrayByteList1.add((byte) 1);
        arrayByteList1.add((byte) 2);

        ArrayByteList arrayByteList2 = new ArrayByteList();
        arrayByteList2.add((byte) 1);
        arrayByteList2.add((byte) 2);

        arrayByteList.equals(arrayByteList1);
        assertEquals(arrayByteList.hashCode(), arrayByteList1.hashCode());
    }
}
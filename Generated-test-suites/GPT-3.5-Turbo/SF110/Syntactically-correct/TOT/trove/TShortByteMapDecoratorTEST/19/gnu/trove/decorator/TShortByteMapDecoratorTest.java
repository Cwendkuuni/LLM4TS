import static org.junit.Assert.*;
import org.junit.*;
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {
  
  private TShortByteMapDecorator decorator;
  private TShortByteMap map;
  
  @Before
  public void setUp() {
    map = new TShortByteMap();
    decorator = new TShortByteMapDecorator(map);
  }
  
  @Test
  public void testPut_NormalCase_ReturnsPreviousValue() {
    Byte value = decorator.put((short)1, (byte)10);
    assertNull(value);
    value = decorator.put((short)1, (byte)20);
    assertEquals((byte)10, (byte)value);
  }
  
  @Test
  public void testPut_NullKey_ReturnsPreviousValue() {
    Byte value = decorator.put(null, (byte)10);
    assertNull(value);
    value = decorator.put(null, (byte)20);
    assertEquals((byte)10, (byte)value);
  }
  
  @Test
  public void testGet_NormalCase_ReturnsValueForKey() {
    decorator.put((short)1, (byte)10);
    Byte value = decorator.get((short)1);
    assertEquals((byte)10, (byte)value);
  }
  
  @Test
  public void testGet_NullKey_ReturnsValueForNullKey() {
    decorator.put(null, (byte)10);
    Byte value = decorator.get(null);
    assertEquals((byte)10, (byte)value);
  }
  
  @Test
  public void testClear_EmptyMap_ReturnsEmptyMap() {
    decorator.clear();
    assertTrue(decorator.isEmpty());
  }
  
  @Test
  public void testClear_NonEmptyMap_ReturnsEmptyMap() {
    decorator.put((short)1, (byte)10);
    decorator.clear();
    assertTrue(decorator.isEmpty());
  }
  
  @Test
  public void testRemove_NormalCase_ReturnsValueForKey() {
    decorator.put((short)1, (byte)10);
    Byte value = decorator.remove((short)1);
    assertEquals((byte)10, (byte)value);
  }
  
  @Test
  public void testRemove_NullKey_ReturnsValueForNullKey() {
    decorator.put(null, (byte)10);
    Byte value = decorator.remove(null);
    assertEquals((byte)10, (byte)value);
  }
  
  @Test
  public void testEntrySet_EmptyMap_ReturnsEmptySet() {
    assertEquals(0, decorator.entrySet().size());
  }
  
  @Test
  public void testEntrySet_NonEmptyMap_ReturnsSetWithCorrectSize() {
    decorator.put((short)1, (byte)10);
    decorator.put((short)2, (byte)20);
    decorator.put((short)3, (byte)30);
    assertEquals(3, decorator.entrySet().size());
  }
  
  @Test
  public void testContainsValue_ExistingValue_ReturnsTrue() {
    decorator.put((short)1, (byte)10);
    assertTrue(decorator.containsValue((byte)10));
  }
  
  @Test
  public void testContainsValue_NonExistingValue_ReturnsFalse() {
    decorator.put((short)1, (byte)10);
    assertFalse(decorator.containsValue((byte)20));
  }
  
  @Test
  public void testContainsKey_ExistingKey_ReturnsTrue() {
    decorator.put((short)1, (byte)10);
    assertTrue(decorator.containsKey((short)1));
  }
  
  @Test
  public void testContainsKey_NonExistingKey_ReturnsFalse() {
    decorator.put((short)1, (byte)10);
    assertFalse(decorator.containsKey((short)2));
  }
  
  @Test
  public void testSize_EmptyMap_ReturnsZero() {
    assertEquals(0, decorator.size());
  }
  
  @Test
  public void testSize_NonEmptyMap_ReturnsCorrectSize() {
    decorator.put((short)1, (byte)10);
    decorator.put((short)2, (byte)20);
    decorator.put((short)3, (byte)30);
    assertEquals(3, decorator.size());
  }
  
  @Test
  public void testIsEmpty_EmptyMap_ReturnsTrue() {
    assertTrue(decorator.isEmpty());
  }
  
  @Test
  public void testIsEmpty_NonEmptyMap_ReturnsFalse() {
    decorator.put((short)1, (byte)10);
    assertFalse(decorator.isEmpty());
  }
  
  @Test
  public void testPutAll_EmptyMap_ReturnsMapWithGivenEntries() {
    DecoratorMap<Short, Byte> inputMap = new HashMap<Short, Byte>();
    inputMap.put((short)1, (byte)10);
    inputMap.put((short)2, (byte)20);
    inputMap.put((short)3, (byte)30);
    decorator.putAll(inputMap);
    assertEquals(3, decorator.size());
  }
  
  @Test
  public void testPutAll_NonEmptyMap_ReturnsMapWithGivenEntriesAddedToExistingEntries() {
    decorator.put((short)1, (byte)10);
    decorator.put((short)2, (byte)20);
    decorator.put((short)3, (byte)30);
    
    DecoratorMap<Short, Byte> inputMap = new HashMap<Short, Byte>();
    inputMap.put((short)4, (byte)40);
    inputMap.put((short)5, (byte)50);
    inputMap.put((short)6, (byte)60);
    
    decorator.putAll(inputMap);
    assertEquals(6, decorator.size());
  }
  
  @Test
  public void testWrapKey_NormalCase_ReturnsKeyAsIs() {
    short key = 10;
    Short wrappedKey = decorator.wrapKey(key);
    assertEquals(key, (short)wrappedKey);
  }
  
  @Test
  public void testUnwrapKey_KeyIsShort_ReturnsKeyAsIs() {
    Short key = 10;
    short unwrappedKey = decorator.unwrapKey(key);
    assertEquals(key, (short)unwrappedKey);
  }
  
  @Test
  public void testWrapValue_NormalCase_ReturnsValueAsIs() {
    byte value = 10;
    Byte wrappedValue = decorator.wrapValue(value);
    assertEquals(value, (byte)wrappedValue);
  }
  
  @Test
  public void testUnwrapValue_ValueIsByte_ReturnsValueAsIs() {
    Byte value = 10;
    byte unwrappedValue = decorator.unwrapValue(value);
    assertEquals(value, (byte)unwrappedValue);
  }
  
  @Test
  public void testReadExternal_NormalCase_PopulatesDecoratorMap() {
    // Write test case here
  }
  
  @Test
  public void testWriteExternal_NormalCase_SerializesDecoratorMap() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    decorator.writeExternal(oos);
    oos.close();
    byte[] serializedData = baos.toByteArray();
    // Verify serialization here
  }
}
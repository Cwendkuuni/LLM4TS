import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;

public class TDoubleShortMapDecoratorTest {
  
  private TDoubleShortMapDecorator testMapDecorator;
  
  @Before
  public void setUp() {
    testMapDecorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
  }
  
  @Test
  public void testPutAndGet() {
    Double key = 1.0;
    Short value = 2;
    
    assertNull(testMapDecorator.put(key, value));
    assertEquals(value, testMapDecorator.put(key, value));
    assertEquals(value, testMapDecorator.get(key));
  }
  
  @Test
  public void testGetWithNullKey() {
    assertNull(testMapDecorator.get(null));
  }
  
  @Test
  public void testRemove() {
    Double key = 1.0;
    Short value = 2;
    
    testMapDecorator.put(key, value);
    assertEquals(value, testMapDecorator.remove(key));
    assertNull(testMapDecorator.remove(key));
  }
  
  @Test
  public void testClear() {
    testMapDecorator.put(1.0, (short)2);
    testMapDecorator.put(2.0, (short)3);
    
    testMapDecorator.clear();
    assertEquals(0, testMapDecorator.size());
  }
  
  @Test
  public void testContainsValue() {
    testMapDecorator.put(1.0, (short)2);
    assertTrue(testMapDecorator.containsValue((short)2));
    assertFalse(testMapDecorator.containsValue((short)3));
  }
  
  @Test
  public void testContainsKey() {
    testMapDecorator.put(1.0, (short)2);
    assertTrue(testMapDecorator.containsKey(1.0));
    assertFalse(testMapDecorator.containsKey(2.0));
  }
  
  @Test
  public void testSizeAndIsEmpty() {
    assertEquals(0, testMapDecorator.size());
    assertTrue(testMapDecorator.isEmpty());
    
    testMapDecorator.put(1.0, (short)2);
    
    assertEquals(1, testMapDecorator.size());
    assertFalse(testMapDecorator.isEmpty());
  }
  
  @Test
  public void testPutAll() {
    Map<Double, Short> map = new HashMap<>();
    map.put(1.0, (short)2);
    map.put(2.0, (short)3);
    
    testMapDecorator.putAll(map);
    
    assertEquals(2, testMapDecorator.size());
    assertTrue(testMapDecorator.containsKey(1.0));
    assertTrue(testMapDecorator.containsKey(2.0));
  }
  
  @Test
  public void testReadExternal() {
    // Not possible to test without mocking Externalizable and ObjectInput
  }
  
  @Test
  public void testWriteExternal() {
    // Not possible to test without mocking Externalizable and ObjectOutput
  }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

  @Test
  public void testAnswer() {
    ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

    // Test for toString() method
    assertEquals("Mock for Object, hashCode: 0", emptyValues.answer(null));

    // Test for compareTo() method
    assertEquals(1, emptyValues.answer(null));

    // Test for primitive return types
    assertEquals(0, emptyValues.answer(null));
    assertEquals((byte) 0, emptyValues.answer(null));
    assertEquals((short) 0, emptyValues.answer(null));
    assertEquals(0L, emptyValues.answer(null));
    assertEquals(0.0f, emptyValues.answer(null));
    assertEquals(0.0, emptyValues.answer(null));
    assertEquals(false, emptyValues.answer(null));
    assertEquals('\u0000', emptyValues.answer(null));

    // Test for primitive wrapper return types
    assertEquals(Integer.valueOf(0), emptyValues.answer(null));
    assertEquals(Byte.valueOf((byte) 0), emptyValues.answer(null));
    assertEquals(Short.valueOf((short) 0), emptyValues.answer(null));
    assertEquals(Long.valueOf(0), emptyValues.answer(null));
    assertEquals(Float.valueOf(0.0f), emptyValues.answer(null));
    assertEquals(Double.valueOf(0.0), emptyValues.answer(null));
    assertEquals(Boolean.FALSE, emptyValues.answer(null));
    assertEquals(Character.valueOf('\u0000'), emptyValues.answer(null));

    // Test for collection return types
    assertTrue(emptyValues.answer(null) instanceof LinkedList);
    assertTrue(emptyValues.answer(null) instanceof HashSet);
    assertTrue(emptyValues.answer(null) instanceof HashSet);
    assertTrue(emptyValues.answer(null) instanceof TreeSet);
    assertTrue(emptyValues.answer(null) instanceof TreeSet);
    assertTrue(emptyValues.answer(null) instanceof LinkedHashSet);
    assertTrue(emptyValues.answer(null) instanceof LinkedList);
    assertTrue(emptyValues.answer(null) instanceof LinkedList);
    assertTrue(emptyValues.answer(null) instanceof ArrayList);
    assertTrue(emptyValues.answer(null) instanceof HashMap);
    assertTrue(emptyValues.answer(null) instanceof HashMap);
    assertTrue(emptyValues.answer(null) instanceof TreeMap);
    assertTrue(emptyValues.answer(null) instanceof TreeMap);
    assertTrue(emptyValues.answer(null) instanceof LinkedHashMap);
  }
}
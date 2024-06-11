import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.core.*;

public class MappingIteratorTest {

  // Test the hasNext() method
  @Test
  public void testHasNext() {
    JsonParser p = new JsonFactory().createParser("[1,2,3]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    try {
      assertTrue(it.hasNext());
      it.next();
      assertTrue(it.hasNext());
      it.next();
      assertTrue(it.hasNext());
      it.next();
      assertFalse(it.hasNext());
    } catch (IOException e) {
      fail("Should not throw IOException");
    }
  }

  // Test the next() method
  @Test
  public void testNext() {
    JsonParser p = new JsonFactory().createParser("[1,2,3]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    try {
      assertEquals(Integer.valueOf(1), it.next());
      assertEquals(Integer.valueOf(2), it.next());
      assertEquals(Integer.valueOf(3), it.next());
    } catch (IOException e) {
      fail("Should not throw IOException");
    }
  }

  // Test the next() method when there are no more elements
  @Test(expected = NoSuchElementException.class)
  public void testNextNoSuchElement() {
    JsonParser p = new JsonFactory().createParser("[]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    it.next();
  }

  // Test the remove() method
  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, null, null, null, true, null);
    it.remove();
  }

  // Test the close() method when parser is not null
  @Test
  public void testClose() {
    JsonParser p = new JsonFactory().createParser("[1,2,3]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    try {
      it.close();
    } catch (IOException e) {
      fail("Should not throw IOException");
    }
    assertNull(it.getParser());
  }

  // Test the hasNextValue() method when parser is null
  @Test
  public void testHasNextValueNoParser() {
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, null, null, null, true, null);
    try {
      assertFalse(it.hasNextValue());
    } catch (IOException e) {
      fail("Should not throw IOException");
    }
  }

  // Test the hasNextValue() method when there are more elements
  @Test
  public void testHasNextValueTrue() {
    JsonParser p = new JsonFactory().createParser("[1,2,3]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    try {
      assertTrue(it.hasNextValue());
    } catch (IOException e) {
      fail("Should not throw IOException");
    }
  }

  // Test the hasNextValue() method when there are no more elements
  @Test
  public void testHasNextValueFalse() {
    JsonParser p = new JsonFactory().createParser("[]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    try {
      assertFalse(it.hasNextValue());
    } catch (IOException e) {
      fail("Should not throw IOException");
    }
  }

  // Test the nextValue() method when there are no more elements
  @Test(expected = NoSuchElementException.class)
  public void testNextValueNoSuchElement() {
    JsonParser p = new JsonFactory().createParser("[]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    it.nextValue();
  }

  // Test readAll() method with ArrayList
  @Test
  public void testReadAllArrayList() {
    JsonParser p = new JsonFactory().createParser("[1,2,3]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    try {
      List<Integer> result = it.readAll();
      assertEquals(3, result.size());
      assertEquals(Integer.valueOf(1), result.get(0));
      assertEquals(Integer.valueOf(2), result.get(1));
      assertEquals(Integer.valueOf(3), result.get(2));
    } catch (IOException e) {
      fail("Should not throw IOException");
    }
  }

  // Test readAll() method with LinkedList
  @Test
  public void testReadAllLinkedList() {
    JsonParser p = new JsonFactory().createParser("[1,2,3]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    try {
      LinkedList<Integer> result = it.readAll(new LinkedList<Integer>());
      assertEquals(3, result.size());
      assertEquals(Integer.valueOf(1), result.get(0));
      assertEquals(Integer.valueOf(2), result.get(1));
      assertEquals(Integer.valueOf(3), result.get(2));
    } catch (IOException e) {
      fail("Should not throw IOException");
    }
  }

  // Test getParser() method
  @Test
  public void testGetParser() {
    JsonParser p = new JsonFactory().createParser("[1,2,3]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    assertEquals(p, it.getParser());
  }

  // Test getParserSchema() method
  @Test
  public void testGetParserSchema() {
    JsonParser p = new JsonFactory().createParser("[1,2,3]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    assertNull(it.getParserSchema());
  }

  // Test getCurrentLocation() method
  @Test
  public void testGetCurrentLocation() {
    JsonParser p = new JsonFactory().createParser("[1,2,3]");
    MappingIterator<Integer> it = new MappingIterator<Integer>(null, p, null, null, true, null);
    assertNotNull(it.getCurrentLocation());
  }

}

package com.fasterxml.jackson.databind;

import org.junit.*;
import java.io.IOException;
import java.util.*;

public class MappingIteratorTest {
  
  @Test
  public void testHasNext() throws IOException {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    MappingIterator<Integer> iterator = new MappingIterator<Integer>(TypeFactory.defaultInstance().constructType(Integer.class),
        new JsonFactory().createParser(numbers.toString()), null, null, true, null);
    
    Assert.assertEquals(true, iterator.hasNext());
    
    iterator.nextValue();
    iterator.nextValue();
    
    Assert.assertEquals(true, iterator.hasNext());
    
    iterator.nextValue();
    iterator.nextValue();
    iterator.nextValue();
    
    Assert.assertEquals(false, iterator.hasNext());
    
    iterator.close();
  }
  
  @Test
  public void testNext() throws IOException {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    MappingIterator<Integer> iterator = new MappingIterator<Integer>(TypeFactory.defaultInstance().constructType(Integer.class),
        new JsonFactory().createParser(numbers.toString()), null, null, true, null);
    
    Assert.assertEquals(1, (int) iterator.next());
    Assert.assertEquals(2, (int) iterator.next());
    
    iterator.nextValue();
    
    Assert.assertEquals(3, (int) iterator.next());
    
    iterator.nextValue();
    iterator.nextValue();
    
    Assert.assertEquals(4, (int) iterator.next());
    Assert.assertEquals(5, (int) iterator.next());
    
    iterator.close();
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() throws IOException {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    MappingIterator<Integer> iterator = new MappingIterator<Integer>(TypeFactory.defaultInstance().constructType(Integer.class),
        new JsonFactory().createParser(numbers.toString()), null, null, true, null);
    
    iterator.remove();
  }
  
  @Test
  public void testClose() throws IOException {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    MappingIterator<Integer> iterator = new MappingIterator<Integer>(TypeFactory.defaultInstance().constructType(Integer.class),
        new JsonFactory().createParser(numbers.toString()), null, null, true, null);
    
    iterator.close();
    
    Assert.assertNull(iterator.getParser());
  }
  
  @Test
  public void testReadAll() throws IOException {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    MappingIterator<Integer> iterator = new MappingIterator<Integer>(TypeFactory.defaultInstance().constructType(Integer.class),
        new JsonFactory().createParser(numbers.toString()), null, null, true, null);
    
    List<Integer> result = iterator.readAll();
    
    Assert.assertEquals(numbers, result);
    
    iterator.close();
  }
  
  @Test
  public void testGetParser() throws IOException {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    MappingIterator<Integer> iterator = new MappingIterator<Integer>(TypeFactory.defaultInstance().constructType(Integer.class),
        new JsonFactory().createParser(numbers.toString()), null, null, true, null);
    
    Assert.assertNotNull(iterator.getParser());
    
    iterator.close();
  }
  
  @Test
  public void testGetParserSchema() throws IOException {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    MappingIterator<Integer> iterator = new MappingIterator<Integer>(TypeFactory.defaultInstance().constructType(Integer.class),
        new JsonFactory().createParser(numbers.toString()), null, null, true, null);
    
    Assert.assertNull(iterator.getParserSchema());
    
    iterator.close();
  }
  
  @Test
  public void testGetCurrentLocation() throws IOException {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    MappingIterator<Integer> iterator = new MappingIterator<Integer>(TypeFactory.defaultInstance().constructType(Integer.class),
        new JsonFactory().createParser(numbers.toString()), null, null, true, null);
    
    Assert.assertNotNull(iterator.getCurrentLocation());
    
    iterator.close();
  }
}

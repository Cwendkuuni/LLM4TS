package org.apache.commons.collections4.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;

public class SequencesComparatorTest {
  
  @Test
  public void testConstructor_sequence1Null_sequence2Null() {
    SequencesComparator<Integer> comparator = new SequencesComparator<>(null, null);
    assertNotNull(comparator);
    
    // Test getScript method
    EditScript<Integer> script = comparator.getScript();
    assertNotNull(script);
    assertEquals(0, script.size());
  }
  
  @Test
  public void testConstructor_emptySequences() {
    List<Integer> sequence1 = new ArrayList<>();
    List<Integer> sequence2 = new ArrayList<>();
    
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    assertNotNull(comparator);
    
    // Test getScript method
    EditScript<Integer> script = comparator.getScript();
    assertNotNull(script);
    assertEquals(0, script.size());
  }
  
  @Test
  public void testConstructor_sequence1Empty() {
    List<Integer> sequence1 = new ArrayList<>();
    List<Integer> sequence2 = new ArrayList<>();
    sequence2.add(1);
    sequence2.add(2);
    
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    assertNotNull(comparator);
    
    // Test getScript method
    EditScript<Integer> script = comparator.getScript();
    assertNotNull(script);
    assertEquals(2, script.size());
    assertEquals(new InsertCommand<>(1), script.getCommand(0));
    assertEquals(new InsertCommand<>(2), script.getCommand(1));
  }
  
  @Test
  public void testConstructor_sequence2Empty() {
    List<Integer> sequence1 = new ArrayList<>();
    sequence1.add(1);
    sequence1.add(2);
    List<Integer> sequence2 = new ArrayList<>();
    
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    assertNotNull(comparator);
    
    // Test getScript method
    EditScript<Integer> script = comparator.getScript();
    assertNotNull(script);
    assertEquals(2, script.size());
    assertEquals(new DeleteCommand<>(1), script.getCommand(0));
    assertEquals(new DeleteCommand<>(2), script.getCommand(1));
  }
  
  @Test
  public void testConstructor_equalSequences() {
    List<Integer> sequence1 = new ArrayList<>();
    sequence1.add(1);
    sequence1.add(2);
    List<Integer> sequence2 = new ArrayList<>();
    sequence2.add(1);
    sequence2.add(2);
    
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    assertNotNull(comparator);
    
    // Test getScript method
    EditScript<Integer> script = comparator.getScript();
    assertNotNull(script);
    assertEquals(2, script.size());
    assertEquals(new KeepCommand<>(1), script.getCommand(0));
    assertEquals(new KeepCommand<>(2), script.getCommand(1));
  }
  
  @Test
  public void testConstructor_differentSequences() {
    List<Integer> sequence1 = new ArrayList<>();
    sequence1.add(1);
    sequence1.add(2);
    List<Integer> sequence2 = new ArrayList<>();
    sequence2.add(3);
    sequence2.add(4);
    
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, sequence2);
    assertNotNull(comparator);
    
    // Test getScript method
    EditScript<Integer> script = comparator.getScript();
    assertNotNull(script);
    assertEquals(4, script.size());
    assertEquals(new DeleteCommand<>(1), script.getCommand(0));
    assertEquals(new DeleteCommand<>(2), script.getCommand(1));
    assertEquals(new InsertCommand<>(3), script.getCommand(2));
    assertEquals(new InsertCommand<>(4), script.getCommand(3));
  }
  
  @Test
  public void testConstructor_sequence1Null_sequence2Provided() {
    List<Integer> sequence2 = new ArrayList<>();
    sequence2.add(1);
    sequence2.add(2);
    
    SequencesComparator<Integer> comparator = new SequencesComparator<>(null, sequence2);
    assertNotNull(comparator);
    
    // Test getScript method
    EditScript<Integer> script = comparator.getScript();
    assertNotNull(script);
    assertEquals(2, script.size());
    assertEquals(new InsertCommand<>(1), script.getCommand(0));
    assertEquals(new InsertCommand<>(2), script.getCommand(1));
  }
  
  @Test
  public void testConstructor_sequence2Null_sequence1Provided() {
    List<Integer> sequence1 = new ArrayList<>();
    sequence1.add(1);
    sequence1.add(2);
    
    SequencesComparator<Integer> comparator = new SequencesComparator<>(sequence1, null);
    assertNotNull(comparator);
    
    // Test getScript method
    EditScript<Integer> script = comparator.getScript();
    assertNotNull(script);
    assertEquals(2, script.size());
    assertEquals(new DeleteCommand<>(1), script.getCommand(0));
    assertEquals(new DeleteCommand<>(2), script.getCommand(1));
  }

}
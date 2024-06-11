package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;
import java.util.*;

public class PredicatesTest {
  
  @Test
  public void testAlwaysTrue() {
    Predicate<Integer> alwaysTrue = Predicates.alwaysTrue();
    assertTrue(alwaysTrue.apply(5));
  }
  
  @Test
  public void testAlwaysFalse() {
    Predicate<Integer> alwaysFalse = Predicates.alwaysFalse();
    assertFalse(alwaysFalse.apply(10));
  }
  
  @Test
  public void testIsNull() {
    Predicate<String> isNull = Predicates.isNull();
    assertTrue(isNull.apply(null));
  }
  
  // Test for edge case where input is not null
  @Test
  public void testIsNull_edge() {
    Predicate<String> isNull = Predicates.isNull();
    assertFalse(isNull.apply("example"));
  }
  
  @Test
  public void testNotNull() {
    Predicate<String> notNull = Predicates.notNull();
    assertTrue(notNull.apply("example"));
  }
  
  // Test for edge case where input is null
  @Test
  public void testNotNull_edge() {
    Predicate<String> notNull = Predicates.notNull();
    assertFalse(notNull.apply(null));
  }
  
  // Test composing two predicates with "not"
  @Test
  public void testNot() {
    Predicate<Integer> greaterThanFive = new Predicate<Integer>() {
      public boolean apply(Integer input) {
        return input > 5;
      }
    };
    Predicate<Integer> notGreaterThanFive = Predicates.not(greaterThanFive);
    assertTrue(notGreaterThanFive.apply(3));
  }
  
  // More test cases for other methods can be added here
  
}
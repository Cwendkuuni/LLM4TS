package com.google.javascript.rhino.jstype.test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.BaseTestCase;
import com.google.javascript.rhino.testing.TestEnvironment;
import org.junit.Before;
import org.junit.Test;

import static com.google.javascript.rhino.jstype.RecordTypeBuilder.*;
import static org.junit.Assert.assertEquals;

public class RecordTypeBuilderTest extends BaseTestCase {

  private TestEnvironment environment;
  private JSTypeRegistry registry;

  @Before
  public void setUp() throws Exception {
    registry = new JSTypeRegistry();
    environment = new TestEnvironment();
  }

  @Test
  public void testSimpleRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("name", StringType.INSTANCE, null);
    builder.addProperty("age", NumberType.INT, null);
    JSType recordType = builder.build();

    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.getProperties().size(), 2);

    JSType recordInstance = context.newRecordType(recordType);
    recordInstance.setProperty("name", "John Doe");
    recordInstance.setProperty("age", 30);

    assertEquals(recordInstance.get("name"), "John Doe");
    assertEquals(recordInstance.get("age"), 30);
  }

  @Test
  public void testDuplicateProperty() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    builder.addProperty("name", StringType.INSTANCE, null);
    builder.addProperty("name", NumberType.INT, null);

    // This should not compile and should throw an error.
    try {
      builder.build();
      fail("Expected an error when adding a duplicate property");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }

  @Test
  public void testEmptyRecord() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();

    assertEquals(recordType.getClass(), ObjectType.class);
  }
}
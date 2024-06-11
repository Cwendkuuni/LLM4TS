package com.fasterxml.jackson.core.json;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import org.junit.*;

public class JsonWriteContextTest {

private static final JsonWriteContext createRootContext() {
return JsonWriteContext.createRootContext();
}

private static final JsonWriteContext createRootContext(DupDetector dd) {
return JsonWriteContext.createRootContext(dd);
}

private static final JsonWriteContext createChildArrayContext(JsonWriteContext parent) {
return parent.createChildArrayContext();
}

private static final JsonWriteContext createChildObjectContext(JsonWriteContext parent) {
return parent.createChildObjectContext();
}

@BeforeClass
public static void setUpBeforeClass() {
// Set up any static things here
}

@AfterClass
public static void tearDownAfterClass() {
// Tear down any static things here
}

@Before
public void setUp() {
// Set up any instance things here
}

@After
public void tearDown() {
// Tear down any instance things here
}

// Test for methods with no arguments

@Test
public void testGetParent() {
JsonWriteContext parent = createRootContext();
JsonWriteContext child = parent.createChildArrayContext();
assertNotNull(child.getParent());
assertEquals(parent, child.getParent());
}

@Test
public void testGetCurrentName() {
JsonWriteContext context = createRootContext();
assertNull(context.getCurrentName());
}

@Test
public void testGetDupDetector() {
JsonWriteContext context = createRootContext();
assertNull(context.getDupDetector());
}

// Test for methods with arguments

@Test
public void testCreateRootContext() {
JsonWriteContext context = createRootContext();
assertNotNull(context);
assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
}

@Test
public void testCreateRootContextWithDupDetector() {
JsonWriteContext context = createRootContext(new NoopDupDetector());
assertNotNull(context);
assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
}

@Test
public void testCreateChildArrayContext() {
JsonWriteContext parent = createRootContext();
JsonWriteContext child = parent.createChildArrayContext();
assertNotNull(child);
assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
assertEquals(parent, child.getParent());
}

@Test
public void testCreateChildObjectContext() {
JsonWriteContext parent = createRootContext();
JsonWriteContext child = parent.createChildObjectContext();
assertNotNull(child);
assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
assertEquals(parent, child.getParent());
}

// Test for methods with side effects

@Test
public void testWriteFieldName_nullName() {
JsonWriteContext context = createRootContext();
try {
context.writeFieldName(null);
fail("Expected JsonProcessingException");
} catch (JsonProcessingException e) {
// Expected
}
}

@Test
public void testWriteFieldName_validName() {
JsonWriteContext context = createRootContext();
context.writeFieldName("name");
assertTrue(context.isGotName());
assertEquals("name", context.getCurrentName());
}

@Test
public void testWriteFieldName_duplicateName() {
JsonWriteContext context = createRootContext(new DupDetector() {
@Override public boolean isDup(String name) {
return name.equals("name");
}
});
context.writeFieldName("name");
context.writeFieldName("name");
try {
context.writeFieldName("name");
fail("Expected JsonGenerationException");
} catch (JsonGenerationException e) {
// Expected
}
}

@Test
public void testWriteValue_rootContext() {
JsonWriteContext context = createRootContext();
context.writeValue();
assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.getStatus());
}

@Test
public void testWriteValue_objectContext() {
JsonWriteContext parent = createRootContext();
JsonWriteContext context = parent.createChildObjectContext();
context.writeValue();
assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
}

@Test
public void testWriteValue_arrayContext() {
JsonWriteContext parent = createRootContext();
JsonWriteContext context = parent.createChildArrayContext();
context.writeValue();
assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.getStatus());
}

// Test for methods that return values

@Test
public void testGetCurrentValue() {
JsonWriteContext context = createRootContext();
context.setCurrentValue(new Object());
Object value = context.getCurrentValue();
assertNotNull(value);
assertEquals(context.getCurrentValue(), value);
}

@Test
public void testGetIndex() {
JsonWriteContext context = createRootContext();
assertEquals(-1, context.getCurrentIndex());
}

// Test for constructors

@Test(expected = IllegalStateException.class)
public void testJsonWriteContext_nullParent() {
JsonWriteContext context = new JsonWriteContext(-1, null, null);
fail("Should not reach this point");
}

@Test
public void testJsonWriteContext_validArguments() {
JsonWriteContext context = new JsonWriteContext(1, createRootContext(), null);
assertEquals(1, context.getType());
assertEquals(null, context.getParent());
assertEquals(null, context.getDupDetector());
}

// Test for reset method

@Test
public void testReset() {
JsonWriteContext context = createRootContext();
context.reset(JsonWriteContext.TYPE_ARRAY);
assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
assertEquals(-1, context.getCurrentIndex());
assertEquals(null, context.getCurrentName());
assertEquals(false, context.isGotName());
assertEquals(null, context.getCurrentValue());
assertEquals(null, context.getDupDetector());
}

// Test for withDupDetector method

@Test
public void testWithDupDetector_nullDupDetector() {
JsonWriteContext context = createRootContext();
context.withDupDetector(null);
assertEquals(null, context.getDupDetector());
}

@Test
public void testWithDupDetector_validDupDetector() {
JsonWriteContext context = createRootContext();
DupDetector dupDetector = new DupDetector() {
@Override public boolean isDup(String name) {
return false;
}
};
context.withDupDetector(dupDetector);
assertEquals(dupDetector, context.getDupDetector());
}

}
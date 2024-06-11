
import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {

@Test
public void testAddProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);
    assertNotNull(result);
}

@Test
public void testAddPropertyDuplicate() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    RecordTypeBuilder result = builder.addProperty("name", type, propertyNode);
    assertNull(result);
}

@Test
public void testBuildEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType result = builder.build();
    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
}

@Test
public void testBuildNonEmptyRecord() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
    Node propertyNode = new Node();
    builder.addProperty("name", type, propertyNode);
    JSType result = builder.build();
    // assert something about the result
    // e.g. assertEquals(expected, result);
    // or assertTrue(result.something());
}

}

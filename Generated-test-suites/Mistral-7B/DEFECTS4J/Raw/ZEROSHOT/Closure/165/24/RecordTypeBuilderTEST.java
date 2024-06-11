import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddProperty_ValidArguments_PropertyAddedToBuilder() {
    // Given
    String name = "propertyName";
    JSType type = registry.getSimpleType("String");
    Node propertyNode = null; // For simplicity, we won't test this argument

    // When
    RecordProperty recordProperty = recordTypeBuilder.addProperty(name, type, propertyNode);

    // Then
    assertTrue(recordProperty != null);
    Map<String, RecordProperty> expectedProperties = Maps.newHashMap();
    expectedProperties.put(name, recordProperty);
    JSType expectedRecordType = new RecordType(registry, expectedProperties);
    JSType actualRecordType = recordTypeBuilder.build();
    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testAddProperty_DuplicatePropertyName_NullReturned() {
    // Given
    String name = "propertyName";
    JSType type = registry.getSimpleType("String");
    String duplicateName = name;

    // When
    RecordProperty firstProperty = recordTypeBuilder.addProperty(name, type, null);

    // Then
    RecordProperty secondProperty = recordTypeBuilder.addProperty(duplicateName, type, null);

    // Assert that the second property addition returns null
    assertNull(secondProperty);

    // And assert that the builder's properties map contains only one record property
    Map<String, RecordProperty> expectedProperties = Maps.newHashMap();
    expectedProperties.put(name, firstProperty);
    JSType expectedRecordType = new RecordType(registry, expectedProperties);
    JSType actualRecordType = recordTypeBuilder.build();
    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test
  public void testBuild_EmptyRecord_ReturnsNativeObjectType() {
    // Given
    JSType expectedNativeObjectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    // When
    JSType actualRecordType = recordTypeBuilder.build();

    // Then
    assertEquals(expectedNativeObjectType, actualRecordType);
  }

  @Test
  public void testBuild_NonEmptyRecord_ReturnsRecordType() {
    // Given
    String name1 = "propertyName1";
    String name2 = "propertyName2";
    JSType type1 = registry.getSimpleType("String");
    JSType type2 = registry.getSimpleType("Number");
    Node propertyNode1 = null; // For simplicity, we won't test this argument
    Node propertyNode2 = null; // For simplicity, we won't test this argument

    // When
    RecordProperty property1 = recordTypeBuilder.addProperty(name1, type1, propertyNode1);
    RecordProperty property2 = recordTypeBuilder.addProperty(name2, type2, propertyNode2);
    JSType actualRecordType = recordTypeBuilder.build();

    // Then
    Map<String, RecordProperty> expectedProperties = Maps.newHashMap();
    expectedProperties.put(name1, property1);
    expectedProperties.put(name2, property2);
    JSType expectedRecordType = new RecordType(registry, expectedProperties);
    assertEquals(expectedRecordType, actualRecordType);
  }
}
import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for RecordTypeBuilder
 */
public class RecordTypeBuilderTest {

  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  // Test addProperty method

  @Test
  public void testAddProperty() {
    JSType stringType = registry.getJSStringType();
    RecordProperty recordProperty = recordTypeBuilder.addProperty("name", stringType, null);
    assertTrue(recordProperty != null);
    assertEquals(stringType, recordProperty.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithDuplicateName() {
    JSType stringType = registry.getJSStringType();
    RecordProperty recordProperty1 = recordTypeBuilder.addProperty("name", stringType, null);
    RecordProperty recordProperty2 = recordTypeBuilder.addProperty("name", stringType, null);
  }

  @Test
  public void testBuildEmptyRecordType() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    JSType builtRecordType = recordTypeBuilder.build();
    assertEquals(objectType, builtRecordType);
  }

  @Test
  public void testBuildRecordTypeWithProperties() {
    JSType stringType = registry.getJSStringType();
    JSType numberType = registry.getJSNumberType();
    RecordProperty property1 = recordTypeBuilder.addProperty("name", stringType, null);
    RecordProperty property2 = recordTypeBuilder.addProperty("age", numberType, null);
    JSType builtRecordType = recordTypeBuilder.build();

    Map<String, RecordProperty> properties = ((RecordType) builtRecordType).getProperties();
    assertEquals(2, properties.size());
    RecordProperty propertyName = properties.get("name");
    RecordProperty propertyAge = properties.get("age");

    assertEquals(stringType, propertyName.getType());
    assertEquals(numberType, propertyAge.getType());
  }
}
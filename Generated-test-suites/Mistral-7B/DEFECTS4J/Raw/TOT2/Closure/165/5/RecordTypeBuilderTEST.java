import static org.junit.Assert.*;
import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.jstype.*;
import org.junit.*;

public class RecordTypeBuilderTest {
  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setup() {
    registry = new JSTypeRegistry();
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testRecordTypeBuilderConstruction() {
    assertNotNull(recordTypeBuilder);
  }

  @Test
  public void testAddPropertyMethodWithDuplicateName() {
    String propertyName = "duplicateProperty";
    JSType propertyType = JSType.STRING;

    RecordProperty property1 = recordTypeBuilder.addProperty(propertyName, propertyType, null);
    RecordProperty property2 = recordTypeBuilder.addProperty(propertyName, propertyType, null);

    assertNull(property2);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyMethodWithNullName() {
    String propertyName = null;
    JSType propertyType = JSType.STRING;

    recordTypeBuilder.addProperty(propertyName, propertyType, null);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyMethodWithNullType() {
    String propertyName = "testProperty";
    JSType propertyType = null;

    recordTypeBuilder.addProperty(propertyName, propertyType, null);
  }

  @Test(expected = NullPointerException.class)
  public void testAddPropertyMethodWithNullRegistry() {
    JSTypeRegistry registry = null;
    String propertyName = "testProperty";
    JSType propertyType = JSType.STRING;

    RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
    recordTypeBuilder.addProperty(propertyName, propertyType, null);
  }

  @Test
  public void testAddPropertyMethod() {
    String propertyName = "testProperty";
    JSType propertyType = JSType.STRING;

    RecordProperty expectedRecordProperty = new RecordProperty(propertyType, null);
    RecordProperty actualRecordProperty = recordTypeBuilder.addProperty(propertyName, propertyType, null);

    assertNotNull(actualRecordProperty);
    assertEquals(expectedRecordProperty.getType(), actualRecordProperty.getType());
  }

  @Test
  public void testBuildMethodWithEmptyRecord() {
    JSType expectedRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType actualRecordType = recordTypeBuilder.build();

    assertEquals(expectedRecordType, actualRecordType);
  }

  @Test(expected = IllegalStateException.class)
  public void testBuildMethodWithDuplicateProperties() {
    String propertyName1 = "property1";
    String propertyName2 = "property1";
    JSType propertyType1 = JSType.STRING;
    JSType propertyType2 = JSType.NUMBER;

    RecordProperty property1 = recordTypeBuilder.addProperty(propertyName1, propertyType1, null);
    RecordProperty property2 = recordTypeBuilder.addProperty(propertyName2, propertyType2, null);

    recordTypeBuilder.build();
  }

  @Test
  public void testBuildMethod() {
    String propertyName1 = "property1";
    String propertyName2 = "property2";
    JSType propertyType1 = JSType.STRING;
    JSType propertyType2 = JSType.NUMBER;

    RecordProperty property1 = recordTypeBuilder.addProperty(propertyName1, propertyType1, null);
    RecordProperty property2 = recordTypeBuilder.addProperty(propertyName2, propertyType2, null);

    JSType expectedRecordType = new RecordType(registry, Maps.newHashMap());
    expectedRecordType.putProperty(propertyName1, propertyType1);
    expectedRecordType.putProperty(propertyName2, propertyType2);

    JSType actualRecordType = recordTypeBuilder.build();

    assertEquals(expectedRecordType, actualRecordType);
  }
}
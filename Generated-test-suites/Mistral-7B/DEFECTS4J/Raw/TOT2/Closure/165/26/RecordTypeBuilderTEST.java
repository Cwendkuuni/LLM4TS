import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.After;
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
  public void testRecordTypeBuilderConstructor() {
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    assertNotNull(builder);
  }

  @Test
  public void testAddPropertyMethod() {
    String propertyName = "propertyName";
    JSType propertyType = registry.createStringType();
    Node propertyNode = new Node();
    RecordProperty expectedRecordProperty = new RecordProperty(propertyType, propertyNode);

    RecordProperty actualRecordProperty = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
    assertEquals(expectedRecordProperty, actualRecordProperty);
  }

  @Test
  public void testBuildMethod() {
    String propertyName1 = "propertyName1";
    String propertyName2 = "propertyName2";
    JSType propertyType1 = registry.createStringType();
    JSType propertyType2 = registry.createNumberType();
    Node propertyNode1 = new Node();
    Node propertyNode2 = new Node();

    RecordProperty property1 = recordTypeBuilder.addProperty(propertyName1, propertyType1, propertyNode1);
    RecordProperty property2 = recordTypeBuilder.addProperty(propertyName2, propertyType2, propertyNode2);

    JSType recordType = recordTypeBuilder.build();

    RecordType expectedRecordType = new RecordType(registry, Collections.singletonMap(propertyName1, property1),
        Collections.singletonMap(propertyName2, property2));

    assertEquals(expectedRecordType, recordType);
  }

  @After
  public void tearDown() {
    recordTypeBuilder = null;
  }
}
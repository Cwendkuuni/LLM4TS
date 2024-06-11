import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.test.TypeChecker;
import com.google.javascript.rhino.test.TypeCheckerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RecordTypeBuilderTest {

  @Test
  public void testRecordTypeBuilderEmpty() {
    JSTypeRegistry registry = TypeCheckerFactory.getFactory().createTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);
    JSType recordType = builder.build();

    TypeChecker typeChecker = new TypeChecker(registry);
    assertNotNull(recordType);
    assertEquals(JSTypeNative.OBJECT_TYPE, recordType.getBaseType());
  }

  @Test
  public void testAddProperty() {
    JSTypeRegistry registry = TypeCheckerFactory.getFactory().createTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "property1";
    JSType propertyType = TypeCheckerFactory.booleanType();
    Node propertyNode = newObjectLiteralNode(propertyName, propertyType);

    RecordTypeBuilder returnedBuilder = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNotNull(returnedBuilder);

    JSType recordType = builder.build();
    Map<String, JSType> properties = recordType.asRecordType().getProperties();
    assertTrue(properties.containsKey(propertyName));
    assertEquals(propertyType, properties.get(propertyName).getValueType());
  }

  @Test
  public void testAddDuplicateProperty() {
    JSTypeRegistry registry = TypeCheckerFactory.getFactory().createTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "property1";
    JSType propertyType = TypeCheckerFactory.booleanType();
    Node propertyNode = newObjectLiteralNode(propertyName, propertyType);

    RecordTypeBuilder builder1 = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNotNull(builder1);

    RecordTypeBuilder builder2 = builder.addProperty(propertyName, propertyType, propertyNode);
    assertNull(builder2);

    JSType recordType = builder.build();
    Map<String, JSType> properties = recordType.asRecordType().getProperties();
    assertEquals(1, properties.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullName() {
    JSTypeRegistry registry = TypeCheckerFactory.getFactory().createTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = null;
    JSType propertyType = TypeCheckerFactory.booleanType();
    Node propertyNode = newObjectLiteralNode(propertyName, propertyType);

    builder.addProperty(propertyName, propertyType, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullType() {
    JSTypeRegistry registry = TypeCheckerFactory.getFactory().createTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "property1";
    JSType propertyType = null;
    Node propertyNode = newObjectLiteralNode(propertyName, propertyType);

    builder.addProperty(propertyName, propertyType, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyNullPropertyNode() {
    JSTypeRegistry registry = TypeCheckerFactory.getFactory().createTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "property1";
    JSType propertyType = TypeCheckerFactory.booleanType();
    Node propertyNode = null;

    builder.addProperty(propertyName, propertyType, propertyNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyDuplicatePropertyNode() {
    JSTypeRegistry registry = TypeCheckerFactory.getFactory().createTypeRegistry();
    RecordTypeBuilder builder = new RecordTypeBuilder(registry);

    String propertyName = "property1";
    JSType propertyType = TypeCheckerFactory.booleanType();
    Node propertyNode = newObjectLiteralNode(propertyName, propertyType);

    builder.addProperty(propertyName, propertyType, propertyNode);
    builder.addProperty(propertyName, propertyType, propertyNode);
  }
}
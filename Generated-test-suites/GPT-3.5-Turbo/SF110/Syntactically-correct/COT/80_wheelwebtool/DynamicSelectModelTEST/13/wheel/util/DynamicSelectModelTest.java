
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.components.ElExpression;
import wheel.WheelException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;

public class DynamicSelectModelTest {
  
  private DynamicSelectModel dynamicSelectModel;
  
  @Before
  public void setUp() {
    dynamicSelectModel = new DynamicSelectModel();
  }
  
  @Test
  public void testConstructorWithArgs() {
    ElExpression collection = new ElExpression("collection");
    ElExpression label = new ElExpression("label");
    ElExpression value = new ElExpression("value");
    DynamicSelectModel dsModel = new DynamicSelectModel(collection, label, value);
    assertEquals(collection, dsModel.collection);
    assertEquals(label, dsModel.label);
    assertEquals(value, dsModel.value);
  }
  
  @Test
  public void testConstructorWithoutArgs() {
    DynamicSelectModel dsModel = new DynamicSelectModel();
    assertNull(dsModel.label);
    assertNull(dsModel.value);
    assertNull(dsModel.collection);
  }
  
  @Test
  public void testCollection() {
    DynamicSelectModel dsModel = dynamicSelectModel.collection("collection");
    assertEquals("collection", dsModel.collection.getExpression());
  }
  
  @Test
  public void testLabel() {
    DynamicSelectModel dsModel = dynamicSelectModel.label("label");
    assertEquals("label", dsModel.label.getExpression());
  }
  
  @Test
  public void testValue() {
    DynamicSelectModel dsModel = dynamicSelectModel.value("value");
    assertEquals("value", dsModel.value.getExpression());
  }
  
  @Test
  public void testEnumeration() {
    DynamicSelectModel dsModel = dynamicSelectModel.enumeration("enumeration");
    assertEquals("enumeration.values()", dsModel.collection.getExpression());
    assertEquals("toString()", dsModel.label.getExpression());
    assertEquals("ordinal()", dsModel.value.getExpression());
  }
  
  @Test
  public void testTranslator() {
    DynamicSelectModel dsModel = dynamicSelectModel.translator("translator");
    assertEquals("translator", dsModel.translator.getExpression());
  }
  
  @Test
  public void testEmpty() {
    Object obj = new Object();
    DynamicSelectModel dsModel = dynamicSelectModel.empty(obj);
    assertEquals(obj, dsModel.emptyObject);
  }
  
  @Test
  public void testGetOptionCount() {
    Collection<Object> objects = new ArrayList<Object>();
    objects.add(new Object());
    objects.add(new Object());
    objects.add(new Object());
    dynamicSelectModel.objects = objects;
    assertEquals(3, dynamicSelectModel.getOptionCount());
  }
  
  @Test
  public void testGetLabel() {
    StandaloneComponent topLevelComponent = new StandaloneComponent();
    Component component = new Component();
    dynamicSelectModel.topLevelComponent = topLevelComponent;
    dynamicSelectModel.component = component;
    dynamicSelectModel.label = new ElExpression("label");
    ArrayList<Object> objects = new ArrayList<Object>();
    objects.add("label1");
    objects.add("label2");
    objects.add("label3");
    dynamicSelectModel.objects = objects;
    assertEquals("label1", dynamicSelectModel.getLabel(0));
    assertEquals("label2", dynamicSelectModel.getLabel(1));
    assertEquals("label3", dynamicSelectModel.getLabel(2));
  }
  
  @Test
  public void testGetValue() {
    StandaloneComponent topLevelComponent = new StandaloneComponent();
    Component component = new Component();
    dynamicSelectModel.topLevelComponent = topLevelComponent;
    dynamicSelectModel.component = component;
    dynamicSelectModel.value = new ElExpression("value");
    ArrayList<Object> objects = new ArrayList<Object>();
    objects.add("value1");
    objects.add("value2");
    objects.add("value3");
    dynamicSelectModel.objects = objects;
    assertEquals("value1", dynamicSelectModel.getValue(0));
    assertEquals("value2", dynamicSelectModel.getValue(1));
    assertEquals("value3", dynamicSelectModel.getValue(2));
  }
  
  @Test
  public void testTranslateValue() {
    StandaloneComponent topLevelComponent = new StandaloneComponent();
    Component component = new Component();
    dynamicSelectModel.topLevelComponent = topLevelComponent;
    dynamicSelectModel.component = component;
    dynamicSelectModel.value = new ElExpression("value");
    ArrayList<Object> objects = new ArrayList<Object>();
    objects.add("value1");
    objects.add("value2");
    objects.add("value3");
    dynamicSelectModel.objects = objects;
    assertEquals("value1", dynamicSelectModel.translateValue("value1"));
    assertNull(dynamicSelectModel.translateValue("value4"));
  }
  
  @Test
  public void testGetObjects() {
    StandaloneComponent topLevelComponent = new StandaloneComponent();
    Component component = new Component();
    dynamicSelectModel.topLevelComponent = topLevelComponent;
    dynamicSelectModel.component = component;
    dynamicSelectModel.collection = new ElExpression("collection");
    ArrayList<Object> objects = new ArrayList<Object>();
    objects.add("object1");
    objects.add("object2");
    objects.add("object3");
    dynamicSelectModel.objects = objects;
    assertEquals(objects, dynamicSelectModel.getObjects());
  }
  
  @Test(expected = WheelException.class)
  public void testGetObjectsNull() {
    dynamicSelectModel.collection = new ElExpression("collection");
    dynamicSelectModel.getObjects();
  }
  
  @Test(expected = WheelException.class)
  public void testGetObjectsArray() {
    StandaloneComponent topLevelComponent = new StandaloneComponent();
    Component component = new Component();
    dynamicSelectModel.topLevelComponent = topLevelComponent;
    dynamicSelectModel.component = component;
    dynamicSelectModel.collection = new ElExpression("collection");
    dynamicSelectModel.objects = new Object[3];
    dynamicSelectModel.getObjects();
  }
  
  @Test(expected = WheelException.class)
  public void testGetObjectsEmpty() {
    StandaloneComponent topLevelComponent = new StandaloneComponent();
    Component component = new Component();
    dynamicSelectModel.topLevelComponent = topLevelComponent;
    dynamicSelectModel.component = component;
    dynamicSelectModel.collection = new ElExpression("collection");
    dynamicSelectModel.getObjects();
  }
  
  @Test
  public void testGetComponent() {
    Component component = new Component();
    dynamicSelectModel.component = component;
    assertEquals(component, dynamicSelectModel.getComponent());
  }
  
  @Test
  public void testSetComponent() {
    Component component = new Component();
    dynamicSelectModel.setComponent(component);
    assertEquals(component, dynamicSelectModel.getComponent());
  }
  
  @Test
  public void testGetTopLevelComponent() {
    StandaloneComponent topLevelComponent = new StandaloneComponent();
    dynamicSelectModel.topLevelComponent = topLevelComponent;
    assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
  }
  
  @Test
  public void testSetTopLevelComponent() {
    StandaloneComponent topLevelComponent = new StandaloneComponent();
    dynamicSelectModel.setTopLevelComponent(topLevelComponent);
    assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
  }
  
  @Test(expected = WheelException.class)
  public void testValidateNullCollection() {
    dynamicSelectModel.label = new ElExpression("label");
    dynamicSelectModel.value = new ElExpression("value");
    dynamicSelectModel.validate();
  }
  
  @Test(expected = WheelException.class)
  public void testValidateNullLabel() {
    dynamicSelectModel.collection = new ElExpression("collection");
    dynamicSelectModel.value = new ElExpression("value");
    dynamicSelectModel.validate();
  }
  
  @Test(expected = WheelException.class)
  public void testValidateNullValue() {
    dynamicSelectModel.collection = new ElExpression("collection");
    dynamicSelectModel.label = new ElExpression("label");
    dynamicSelectModel.validate();
  }
  
  @Test
  public void testReset() {
    dynamicSelectModel.objects = new ArrayList<Object>();
    dynamicSelectModel.reset();
    assertNull(dynamicSelectModel.objects);
  }
}

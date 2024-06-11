
package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import wheel.components.Component;

import java.util.ArrayList;
import java.util.Collection;

public class DynamicSelectModelTest {
  
  @Test
  public void testConstructorWithArgs() {
    ElExpression collection = new ElExpression("collection");
    ElExpression label = new ElExpression("label");
    ElExpression value = new ElExpression("value");
    DynamicSelectModel model = new DynamicSelectModel(collection, label, value);
    assertEquals(collection, model.getCollection());
    assertEquals(label, model.getLabel());
    assertEquals(value, model.getValue());
    assertNull(model.getTranslator());
    assertNull(model.getEmptyObject());
  }
  
  @Test
  public void testDefaultConstructor() {
    DynamicSelectModel model = new DynamicSelectModel();
    assertNull(model.getCollection());
    assertNull(model.getLabel());
    assertNull(model.getValue());
    assertNull(model.getTranslator());
    assertNull(model.getEmptyObject());
  }
  
  @Test
  public void testCollectionSetter() {
    DynamicSelectModel model = new DynamicSelectModel();
    ElExpression collection = new ElExpression("collection");
    model.setCollection(collection);
    assertEquals(collection, model.getCollection());
  }
  
  @Test
  public void testLabelSetter() {
    DynamicSelectModel model = new DynamicSelectModel();
    ElExpression label = new ElExpression("label");
    model.setLabel(label);
    assertEquals(label, model.getLabel());
  }
  
  @Test
  public void testValueSetter() {
    DynamicSelectModel model = new DynamicSelectModel();
    ElExpression value = new ElExpression("value");
    model.setValue(value);
    assertEquals(value, model.getValue());
  }
  
  @Test
  public void testTranslatorSetter() {
    DynamicSelectModel model = new DynamicSelectModel();
    ElExpression translator = new ElExpression("translator");
    model.setTranslator(translator);
    assertEquals(translator, model.getTranslator());
  }
  
  @Test
  public void testEmptyObjectSetter() {
    DynamicSelectModel model = new DynamicSelectModel();
    Object emptyObject = new Object();
    model.setEmptyObject(emptyObject);
    assertEquals(emptyObject, model.getEmptyObject());
  }
  
  @Test
  public void testGetOptionCount() {
    DynamicSelectModel model = new DynamicSelectModel();
    Collection objects = new ArrayList();
    objects.add(new Object());
    objects.add(new Object());
    model.setObjects(objects);
    assertEquals(objects.size(), model.getOptionCount());
  }
  
  @Test
  public void testGetLabel() {
    DynamicSelectModel model = new DynamicSelectModel();
    ElExpression label = new ElExpression("label");
    model.setLabel(label);
    Collection objects = new ArrayList();
    Object obj = new Object();
    objects.add(obj);
    model.setObjects(objects);
    Component component = new Component();
    model.setComponent(component);
    assertEquals(obj.toString(), model.getLabel(0));
  }
  
  @Test
  public void testGetValue() {
    DynamicSelectModel model = new DynamicSelectModel();
    ElExpression value = new ElExpression("value");
    model.setValue(value);
    Collection objects = new ArrayList();
    Object obj = new Object();
    objects.add(obj);
    model.setObjects(objects);
    Component component = new Component();
    model.setComponent(component);
    assertEquals(obj.toString(), model.getValue(0));
  }
  
  @Test
  public void testTranslateValue() {
    DynamicSelectModel model = new DynamicSelectModel();
    ElExpression value = new ElExpression("value");
    model.setValue(value);
    ElExpression translator = new ElExpression("translator");
    model.setTranslator(translator);
    Collection objects = new ArrayList();
    Object obj = new Object();
    objects.add(obj);
    model.setObjects(objects);
    Component component = new Component();
    model.setComponent(component);
    assertEquals(obj, model.translateValue(obj.toString()));
  }
  
  @Test
  public void testGetObjects() {
    DynamicSelectModel model = new DynamicSelectModel();
    ElExpression collection = new ElExpression("collection");
    model.setCollection(collection);
    Collection objects = new ArrayList();
    model.setObjects(objects);
    assertEquals(objects, model.getObjects());
  }
  
  @Test
  public void testGetComponent() {
    DynamicSelectModel model = new DynamicSelectModel();
    Component component = new Component();
    model.setComponent(component);
    assertEquals(component, model.getComponent());
  }
  
  @Test
  public void testSetComponent() {
    DynamicSelectModel model = new DynamicSelectModel();
    Component component = new Component();
    model.setComponent(component);
    assertEquals(component, model.getComponent());
  }
  
  @Test
  public void testGetTopLevelComponent() {
    DynamicSelectModel model = new DynamicSelectModel();
    StandaloneComponent topLevelComponent = new StandaloneComponent();
    model.setTopLevelComponent(topLevelComponent);
    assertEquals(topLevelComponent, model.getTopLevelComponent());
  }
  
  @Test
  public void testSetTopLevelComponent() {
    DynamicSelectModel model = new DynamicSelectModel();
    StandaloneComponent topLevelComponent = new StandaloneComponent();
    model.setTopLevelComponent(topLevelComponent);
    assertEquals(topLevelComponent, model.getTopLevelComponent());
  }
  
  @Test
  public void testValidate() {
    DynamicSelectModel model = new DynamicSelectModel();
    model.setCollection(null);
    try {
      model.validate();
    } catch (WheelException e) {
      assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
    }
    model.setCollection(new ElExpression("collection"));
    model.setLabel(null);
    try {
      model.validate();
    } catch (WheelException e) {
      assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
    }
    model.setLabel(new ElExpression("label"));
    model.setValue(null);
    try {
      model.validate();
    } catch (WheelException e) {
      assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
    }
  }
  
  @Test
  public void testReset() {
    DynamicSelectModel model = new DynamicSelectModel();
    Collection objects = new ArrayList();
    objects.add(new Object());
    model.setObjects(objects);
    model.reset();
    assertNull(model.getObjects());
  }
     
}

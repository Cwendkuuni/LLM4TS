package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.ElExpression;
import wheel.ISelectModel;
import wheel.WheelException;

public class DynamicSelectModelTest {
  
  private DynamicSelectModel selectModel;
  private ElExpression collection;
  private ElExpression label;
  private ElExpression value;
  private Component component;
  private StandaloneComponent topLevelComponent;
  
  @Before
  public void setUp() {
    this.collection = mock(ElExpression.class);
    this.label = mock(ElExpression.class);
    this.value = mock(ElExpression.class);
    
    this.selectModel = new DynamicSelectModel(collection, label, value);
    
    this.component = mock(Component.class);
    this.topLevelComponent = mock(StandaloneComponent.class);
    
    selectModel.setComponent(component);
    selectModel.setTopLevelComponent(topLevelComponent);
  }
  
  @Test
  public void testConstructor() {
    assertEquals(collection, selectModel.collection);
    assertEquals(label, selectModel.label);
    assertEquals(value, selectModel.value);
    selectModel.validate();
  }
  
  @Test
  public void testCollection() {
    String collectionString = "collection";
    selectModel.collection(collectionString);
    assertEquals(new ElExpression(collectionString), selectModel.collection);
  }
  
  @Test
  public void testLabel() {
    String labelString = "label";
    selectModel.label(labelString);
    assertEquals(new ElExpression(labelString), selectModel.label);
  }
  
  @Test
  public void testValue() {
    String valueString = "value";
    selectModel.value(valueString);
    assertEquals(new ElExpression(valueString), selectModel.value);
  }
  
  @Test
  public void testEnumeration() {
    String enumerationString = "enumeration";
    selectModel.enumeration(enumerationString);
    assertEquals(new ElExpression(enumerationString + ".values()"), selectModel.collection);
    assertEquals(new ElExpression("toString()"), selectModel.label);
    assertEquals(new ElExpression("ordinal()"), selectModel.value);
  }
  
  @Test
  public void testTranslator() {
    String translatorString = "translator";
    selectModel.translator(translatorString);
    assertEquals(new ElExpression(translatorString), selectModel.translator);
  }
  
  @Test
  public void testEmpty() {
    Object emptyObject = new Object();
    selectModel.empty(emptyObject);
    assertEquals(emptyObject, selectModel.emptyObject);
  }
  
  @Test
  public void testGetOptionCount() {
    Collection<Object> objects = new ArrayList<Object>();
    objects.add(new Object());
    objects.add(new Object());
    when(collection.eval(topLevelComponent, component)).thenReturn(objects);
    
    int expectedCount = objects.size();
    int actualCount = selectModel.getOptionCount();
    
    assertEquals(expectedCount, actualCount);
  }
  
  @Test
  public void testGetLabel() {
    Collection<Object> objects = new ArrayList<Object>();
    objects.add(new Object());
    objects.add(new Object());
    when(collection.eval(topLevelComponent, component)).thenReturn(objects);
    
    Object obj = new Object();
    when(objects.toArray()).thenReturn(new Object[] { obj });
    
    String expectedLabel = "label";
    when(label.eval(obj, component)).thenReturn(expectedLabel);
    
    String actualLabel = selectModel.getLabel(0);
    
    assertEquals(expectedLabel, actualLabel);
    verify(label).errorMessage(anyString());
    verify(objects).toArray();
  }
  
  @Test
  public void testGetValue() {
    Collection<Object> objects = new ArrayList<Object>();
    objects.add(new Object());
    objects.add(new Object());
    when(collection.eval(topLevelComponent, component)).thenReturn(objects);
    
    Object obj = new Object();
    when(objects.toArray()).thenReturn(new Object[] { obj });
    
    String expectedValue = "value";
    when(value.eval(obj, component)).thenReturn(expectedValue);
    
    String actualValue = selectModel.getValue(0);
    
    assertEquals(expectedValue, actualValue);
    verify(value).errorMessage(anyString());
    verify(objects).toArray();
  }
  
  @Test
  public void testTranslateValue() {
    Collection<Object> objects = new ArrayList<Object>();
    objects.add(new Object());
    objects.add(new Object());
    when(collection.eval(topLevelComponent, component)).thenReturn(objects);
    
    Object obj = new Object();
    when(objects.toArray()).thenReturn(new Object[] { obj });
    
    String valueString = "value";
    when(this.value.eval(obj, component)).thenReturn(valueString);
    
    String expectedTranslatedValue = "translatedValue";
    when(translator.eval(obj, component)).thenReturn(expectedTranslatedValue);
    
    Object actualTranslatedValue = selectModel.translateValue(valueString);
    
    assertEquals(expectedTranslatedValue, actualTranslatedValue);
    verify(translator).errorMessage(anyString());
    verify(objects).toArray();
  }
  
  @Test
  public void testTranslateValueNotFound() {
    Collection<Object> objects = new ArrayList<Object>();
    objects.add(new Object());
    objects.add(new Object());
    when(collection.eval(topLevelComponent, component)).thenReturn(objects);
    
    Object obj = new Object();
    when(objects.toArray()).thenReturn(new Object[] { obj });
    
    String valueString = "value";
    when(this.value.eval(obj, component)).thenReturn("otherValue");
    
    Object actualTranslatedValue = selectModel.translateValue(valueString);
    
    assertNull(actualTranslatedValue);
    verify(objects).toArray();
  }
  
  @Test(expected = WheelException.class)
  public void testGetObjectsNull() {
    when(collection.eval(topLevelComponent, component)).thenReturn(null);
    
    selectModel.getObjects();
  }
  
  @Test(expected = WheelException.class)
  public void testGetObjectsNotCollection() {
    when(collection.eval(topLevelComponent, component)).thenReturn(new Object());
    
    selectModel.getObjects();
  }
  
  @Test
  public void testGetObjectsArray() {
    Object[] array = new Object[] { new Object(), new Object() };
    when(collection.eval(topLevelComponent, component)).thenReturn(array);
    
    Collection<Object> expectedObjects = new ArrayList<Object>();
    for (Object item : array) {
      expectedObjects.add(item);
    }
    
    Collection<Object> actualObjects = selectModel.getObjects();
    
    assertEquals(expectedObjects, actualObjects);
  }
  
  @Test
  public void testGetObjectsCollection() {
    Collection<Object> collection = new ArrayList<Object>();
    collection.add(new Object());
    collection.add(new Object());
    when(collection.eval(topLevelComponent, component)).thenReturn(collection);
    
    Collection<Object> actualObjects = selectModel.getObjects();
    
    assertEquals(collection, actualObjects);
  }
  
  @Test
  public void testGetObjectsEmpty() {
    selectModel.empty(new Object());
    Collection<Object> collection = new ArrayList<Object>();
    collection.add(new Object());
    collection.add(new Object());
    when(collection.eval(topLevelComponent, component)).thenReturn(collection);
    
    Collection<Object> expectedObjects = new LinkedList<Object>();
    expectedObjects.add(selectModel.emptyObject);
    expectedObjects.addAll(collection);
    
    Collection<Object> actualObjects = selectModel.getObjects();
    
    assertEquals(expectedObjects, actualObjects);
  }
  
  @Test
  public void testGetComponent() {
    Component actualComponent = selectModel.getComponent();
    
    assertEquals(component, actualComponent);
  }
  
  @Test
  public void testSetComponent() {
    Component newComponent = mock(Component.class);
    
    selectModel.setComponent(newComponent);
    
    Component actualComponent = selectModel.getComponent();
    
    assertEquals(newComponent, actualComponent);
  }
  
  @Test
  public void testGetTopLevelComponent() {
    StandaloneComponent actualTopLevelComponent = selectModel.getTopLevelComponent();
    
    assertEquals(topLevelComponent, actualTopLevelComponent);
  }
  
  @Test
  public void testSetTopLevelComponent() {
    StandaloneComponent newTopLevelComponent = mock(StandaloneComponent.class);
    
    selectModel.setTopLevelComponent(newTopLevelComponent);
    
    StandaloneComponent actualTopLevelComponent = selectModel.getTopLevelComponent();
    
    assertEquals(newTopLevelComponent, actualTopLevelComponent);
  }
  
  @Test(expected = WheelException.class)
  public void testValidateCollectionNull() {
    selectModel.collection = null;
    
    selectModel.validate();
  }
  
  @Test(expected = WheelException.class)
  public void testValidateLabelNull() {
    selectModel.label = null;
    
    selectModel.validate();
  }
  
  @Test(expected = WheelException.class)
  public void testValidateValueNull() {
    selectModel.value = null;
    
    selectModel.validate();
  }
  
  @Test
  public void testReset() {
    selectModel.objects = new ArrayList<Object>();
    
    selectModel.reset();
    
    assertNull(selectModel.objects);
  }
}
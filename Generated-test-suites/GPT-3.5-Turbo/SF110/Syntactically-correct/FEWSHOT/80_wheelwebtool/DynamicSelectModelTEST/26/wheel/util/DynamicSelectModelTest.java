package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {
    
    private DynamicSelectModel dynamicSelectModel;
    private ElExpression label;
    private ElExpression value;
    private ElExpression collection;
    
    @Before
    public void setup() {
        label = mock(ElExpression.class);
        value = mock(ElExpression.class);
        collection = mock(ElExpression.class);
        
        dynamicSelectModel = new DynamicSelectModel(collection, label, value);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(collection, dynamicSelectModel.collection);
        assertEquals(label, dynamicSelectModel.label);
        assertEquals(value, dynamicSelectModel.value);
    }
    
    @Test
    public void testCollection() {
        String collectionString = "collection";
        ElExpression newCollection = mock(ElExpression.class);

        when(newCollection.eval(any(), any())).thenReturn(collectionString);
        
        DynamicSelectModel updatedModel = dynamicSelectModel.collection(collectionString);
        
        assertEquals(newCollection, updatedModel.collection);
        verify(newCollection).eval(any(), any());
    }
    
    @Test
    public void testLabel() {
        String labelString = "label";
        ElExpression newLabel = mock(ElExpression.class);

        when(newLabel.eval(any(), any())).thenReturn(labelString);
        
        DynamicSelectModel updatedModel = dynamicSelectModel.label(labelString);
        
        assertEquals(newLabel, updatedModel.label);
        verify(newLabel).eval(any(), any());
    }
    
    @Test
    public void testValue() {
        String valueString = "value";
        ElExpression newValue = mock(ElExpression.class);

        when(newValue.eval(any(), any())).thenReturn(valueString);
        
        DynamicSelectModel updatedModel = dynamicSelectModel.value(valueString);
        
        assertEquals(newValue, updatedModel.value);
        verify(newValue).eval(any(), any());
    }
    
    @Test
    public void testEnumeration() {
        String enumeration = "enumeration";
        ElExpression expectedCollection = mock(ElExpression.class);
        ElExpression expectedLabel = mock(ElExpression.class);
        ElExpression expectedValue = mock(ElExpression.class);
        
        when(expectedCollection.eval(any(), any())).thenReturn(enumeration + ".values()");
        when(expectedLabel.eval(any(), any())).thenReturn("toString()");
        when(expectedValue.eval(any(), any())).thenReturn("ordinal()");
        
        DynamicSelectModel updatedModel = dynamicSelectModel.enumeration(enumeration);
        
        assertEquals(expectedCollection, updatedModel.collection);
        assertEquals(expectedLabel, updatedModel.label);
        assertEquals(expectedValue, updatedModel.value);
        
        verify(expectedCollection).eval(any(), any());
        verify(expectedLabel).eval(any(), any());
        verify(expectedValue).eval(any(), any());
    }
    
    @Test
    public void testTranslator() {
        String translator = "translator";
        ElExpression newTranslator = mock(ElExpression.class);

        when(newTranslator.eval(any(), any())).thenReturn(translator);
        
        DynamicSelectModel updatedModel = dynamicSelectModel.translator(translator);
        
        assertEquals(newTranslator, updatedModel.translator);
        verify(newTranslator).eval(any(), any());
    }
    
    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        
        DynamicSelectModel updatedModel = dynamicSelectModel.empty(emptyObject);
        
        assertEquals(emptyObject, updatedModel.emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        Collection<Object> objects = mock(Collection.class);
        dynamicSelectModel.objects = objects;
        
        when(objects.size()).thenReturn(5);
        
        int result = dynamicSelectModel.getOptionCount();
        
        assertEquals(5, result);
        verify(objects).size();
    }
    
    @Test
    public void testGetLabel() {
        Collection<Object> objects = mock(Collection.class);
        dynamicSelectModel.objects = objects;
        dynamicSelectModel.component = mock(Component.class);
        
        Object objectAtIndex = mock(Object.class);
        when(objects.toArray()).thenReturn(new Object[]{objectAtIndex});
        
        String labelValue = "label";
        when(label.eval(objectAtIndex, dynamicSelectModel.component)).thenReturn(labelValue);
        
        String result = dynamicSelectModel.getLabel(0);
        
        assertEquals(labelValue, result);
        verify(label).eval(objectAtIndex, dynamicSelectModel.component);
    }
    
    @Test
    public void testGetValue() {
        Collection<Object> objects = mock(Collection.class);
        dynamicSelectModel.objects = objects;
        dynamicSelectModel.component = mock(Component.class);
        
        Object objectAtIndex = mock(Object.class);
        when(objects.toArray()).thenReturn(new Object[]{objectAtIndex});
        
        String valueString = "value";
        when(value.eval(objectAtIndex, dynamicSelectModel.component)).thenReturn(valueString);
        
        String result = dynamicSelectModel.getValue(0);
        
        assertEquals(valueString, result);
        verify(value).eval(objectAtIndex, dynamicSelectModel.component);
    }
    
    @Test
    public void testTranslateValue() {
        Collection<Object> objects = mock(Collection.class);
        dynamicSelectModel.objects = objects;
        
        Object objectAtIndex = mock(Object.class);
        when(objects.toArray()).thenReturn(new Object[]{objectAtIndex});
        
        String valueString = "value";
        when(value.eval(objectAtIndex, dynamicSelectModel.component)).thenReturn(valueString);
        
        String translatorValue = "translator";
        when(dynamicSelectModel.translator.eval(objectAtIndex, dynamicSelectModel.component)).thenReturn(translatorValue);
        
        Object result = dynamicSelectModel.translateValue(valueString);
        
        assertEquals(translatorValue, result);
        verify(value).eval(objectAtIndex, dynamicSelectModel.component);
        verify(dynamicSelectModel.translator).eval(objectAtIndex, dynamicSelectModel.component);
    }
    
    @Test
    public void testTranslateValue_NoTranslator() {
        Collection<Object> objects = mock(Collection.class);
        dynamicSelectModel.objects = objects;
        
        Object objectAtIndex = mock(Object.class);
        when(objects.toArray()).thenReturn(new Object[]{objectAtIndex});
        
        String valueString = "value";
        when(value.eval(objectAtIndex, dynamicSelectModel.component)).thenReturn(valueString);
        
        Object result = dynamicSelectModel.translateValue(valueString);
        
        assertEquals(objectAtIndex, result);
        verify(value).eval(objectAtIndex, dynamicSelectModel.component);
        verifyZeroInteractions(dynamicSelectModel.translator);
    }
    
    @Test
    public void testTranslateValue_NullResult() {
        Collection<Object> objects = mock(Collection.class);
        dynamicSelectModel.objects = objects;
        
        Object objectAtIndex = mock(Object.class);
        when(objects.toArray()).thenReturn(new Object[]{objectAtIndex});
        
        String valueString = "value";
        when(value.eval(objectAtIndex, dynamicSelectModel.component)).thenReturn("notValue");
        
        Object result = dynamicSelectModel.translateValue(valueString);
        
        assertNull(result);
        verify(value).eval(objectAtIndex, dynamicSelectModel.component);
        verifyZeroInteractions(dynamicSelectModel.translator);
    }
    
    @Test
    public void testGetObjects_ObjectsNotNull() {
        Collection<Object> objects = mock(Collection.class);
        dynamicSelectModel.objects = objects;
        
        Collection<Object> result = dynamicSelectModel.getObjects();
        
        assertEquals(objects, result);
        verifyZeroInteractions(collection);
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjects_NullObject() {
        dynamicSelectModel.objects = null;
        dynamicSelectModel.topLevelComponent = mock(StandaloneComponent.class);
        
        when(collection.eval(dynamicSelectModel.topLevelComponent, dynamicSelectModel.component)).thenReturn(null);
        
        dynamicSelectModel.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjects_InvalidObject() {
        dynamicSelectModel.objects = null;
        dynamicSelectModel.topLevelComponent = mock(StandaloneComponent.class);
        
        when(collection.eval(dynamicSelectModel.topLevelComponent, dynamicSelectModel.component)).thenReturn(new Object());
        
        dynamicSelectModel.getObjects();
    }
    
    @Test
    public void testGetObjects_Collection() {
        Collection<Object> expectedObjects = mock(Collection.class);
        dynamicSelectModel.objects = null;
        dynamicSelectModel.topLevelComponent = mock(StandaloneComponent.class);
        
        when(collection.eval(dynamicSelectModel.topLevelComponent, dynamicSelectModel.component)).thenReturn(expectedObjects);
        
        Collection<Object> result = dynamicSelectModel.getObjects();
        
        assertEquals(expectedObjects, result);
    }
    
    @Test
    public void testGetObjects_Array() {
        Object[] objectArray = new Object[]{mock(Object.class)};
        dynamicSelectModel.objects = null;
        dynamicSelectModel.topLevelComponent = mock(StandaloneComponent.class);
        
        when(collection.eval(dynamicSelectModel.topLevelComponent, dynamicSelectModel.component)).thenReturn(objectArray);
        
        Collection<Object> result = dynamicSelectModel.getObjects();
        
        assertEquals(1, result.size());
    }
    
    @Test
    public void testGetObjects_EmptyObject() {
        Object emptyObject = mock(Object.class);
        Collection<Object> expectedObjects = mock(Collection.class);
        dynamicSelectModel.objects = null;
        dynamicSelectModel.topLevelComponent = mock(StandaloneComponent.class);
        dynamicSelectModel.emptyObject = emptyObject;
        
        when(collection.eval(dynamicSelectModel.topLevelComponent, dynamicSelectModel.component)).thenReturn(expectedObjects);
        when(expectedObjects.isEmpty()).thenReturn(false);
        
        Collection<Object> result = dynamicSelectModel.getObjects();
        
        verify(expectedObjects).isEmpty();
        assertEquals(1, result.size());
    }
    
    @Test
    public void testGetComponent() {
        Component component = mock(Component.class);
        dynamicSelectModel.component = component;
        
        Component result = dynamicSelectModel.getComponent();
        
        assertEquals(component, result);
    }
    
    @Test
    public void testSetComponent() {
        Component component = mock(Component.class);
        
        dynamicSelectModel.setComponent(component);
        
        assertEquals(component, dynamicSelectModel.component);
    }
    
    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        dynamicSelectModel.topLevelComponent = topLevelComponent;
        
        StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();
        
        assertEquals(topLevelComponent, result);
    }
    
    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        
        dynamicSelectModel.setTopLevelComponent(topLevelComponent);
        
        assertEquals(topLevelComponent, dynamicSelectModel.topLevelComponent);
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_NullCollection() {
        dynamicSelectModel.collection = null;
        
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_NullLabel() {
        dynamicSelectModel.label = null;
        
        dynamicSelectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidate_NullValue() {
        dynamicSelectModel.value = null;
        
        dynamicSelectModel.validate();
    }
    
    @Test
    public void testReset() {
        dynamicSelectModel.objects = mock(Collection.class);
        
        dynamicSelectModel.reset();
        
        assertNull(dynamicSelectModel.objects);
    }
}
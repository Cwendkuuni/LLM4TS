package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.ElExpression;
import wheel.WheelException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DynamicSelectModelTest {
    private DynamicSelectModel selectModel;
    private Component componentMock;
    private StandaloneComponent topLevelComponentMock;
    
    @Before
    public void setUp() {
        selectModel = new DynamicSelectModel();
        componentMock = mock(Component.class);
        topLevelComponentMock = mock(StandaloneComponent.class);
        selectModel.setComponent(componentMock);
        selectModel.setTopLevelComponent(topLevelComponentMock);
    }
    
    @Test
    public void testConstructorWithCollectionLabelAndValue() {
        ElExpression collection = mock(ElExpression.class);
        ElExpression label = mock(ElExpression.class);
        ElExpression value = mock(ElExpression.class);
        selectModel = new DynamicSelectModel(collection, label, value);
        
        assertEquals(collection, selectModel.collection);
        assertEquals(label, selectModel.label);
        assertEquals(value, selectModel.value);
    }
    
    @Test
    public void testConstructorWithoutParams() {
        selectModel = new DynamicSelectModel();
        assertNull(selectModel.collection);
        assertNull(selectModel.label);
        assertNull(selectModel.value);
    }
    
    @Test
    public void testCollection() {
        String collection = "collection";
        selectModel.collection(collection);
        
        assertEquals(new ElExpression(collection), selectModel.collection);
    }
    
    @Test
    public void testLabel() {
        String label = "label";
        selectModel.label(label);
        
        assertEquals(new ElExpression(label), selectModel.label);
    }
    
    @Test
    public void testValue() {
        String value = "value";
        selectModel.value(value);
        
        assertEquals(new ElExpression(value), selectModel.value);
    }
    
    @Test
    public void testEnumeration() {
        String enumeration = "enumeration";
        selectModel.enumeration(enumeration);
        
        assertEquals(new ElExpression(enumeration + ".values()"), selectModel.collection);
        assertEquals(new ElExpression("toString()"), selectModel.label);
        assertEquals(new ElExpression("ordinal()"), selectModel.value);
    }
    
    @Test
    public void testTranslator() {
        String translator = "translator";
        selectModel.translator(translator);
        
        assertEquals(new ElExpression(translator), selectModel.translator);
    }
    
    @Test
    public void testEmpty() {
        Object o = new Object();
        selectModel.empty(o);
        
        assertEquals(o, selectModel.emptyObject);
    }
    
    @Test
    public void testGetOptionCount() {
        Collection objects = mock(Collection.class);
        selectModel.objects = objects;
        when(objects.size()).thenReturn(3);
        
        int result = selectModel.getOptionCount();
        
        assertEquals(3, result);
    }
    
    @Test
    public void testGetLabel() {
        int index = 2;
        String expression = "expression";
        Object obj = new Object();
        selectModel.collection = mock(ElExpression.class);
        when(selectModel.collection.errorMessage(anyString())).thenReturn(expression);
        when(selectModel.getObjects()).thenReturn(objects);
        when(objects.toArray()).thenReturn(new Object[]{obj});
        when(selectModel.label.eval(obj, componentMock)).thenReturn(obj);
        when(obj.toString()).thenReturn(expression);
        
        String result = selectModel.getLabel(index);
        
        assertEquals(expression, result);
    }
    
    @Test
    public void testGetValue() {
        int index = 2;
        String expression = "expression";
        Object obj = new Object();
        selectModel.value = mock(ElExpression.class);
        when(selectModel.value.errorMessage(anyString())).thenReturn(expression);
        when(selectModel.getObjects()).thenReturn(objects);
        when(objects.toArray()).thenReturn(new Object[]{obj});
        when(selectModel.value.eval(obj, componentMock)).thenReturn(obj);
        when(obj.toString()).thenReturn(expression);
        
        String result = selectModel.getValue(index);
        
        assertEquals(expression, result);
    }
    
    @Test
    public void testTranslateValueWithTranslatorNotNull() {
        String value = "value";
        Object obj = new Object();
        Object[] array = new Object[]{obj};
        selectModel.translator = mock(ElExpression.class);
        when(selectModel.translator.errorMessage(anyString())).thenReturn("");
        when(selectModel.translator.eval(obj, componentMock)).thenReturn(obj);
        when(selectModel.getValue(anyInt())).thenReturn(value);
        when(selectModel.getObjects()).thenReturn(objects);
        when(objects.toArray()).thenReturn(array);
        
        Object result = selectModel.translateValue(value);
        
        assertEquals(obj, result);
    }
    
    @Test
    public void testTranslateValueWithTranslatorNull() {
        String value = "value";
        Object obj = new Object();
        Object[] array = new Object[]{obj};
        when(selectModel.getValue(anyInt())).thenReturn(value);
        when(selectModel.getObjects()).thenReturn(objects);
        when(objects.toArray()).thenReturn(array);
        
        Object result = selectModel.translateValue(value);
        
        assertEquals(obj, result);
    }
    
    @Test
    public void testTranslateValueWithNoMatch() {
        String value = "value";
        Object obj = new Object();
        Object[] array = new Object[]{obj};
        when(selectModel.getValue(anyInt())).thenReturn("otherValue");
        when(selectModel.getObjects()).thenReturn(objects);
        when(objects.toArray()).thenReturn(array);
        
        Object result = selectModel.translateValue(value);
        
        assertNull(result);
    }
    
    @Test
    public void testGetObjectsWithCollectionObjectType() {
        Collection<Object> collection = mock(Collection.class);
        selectModel.collection = mock(ElExpression.class);
        when(selectModel.collection.errorMessage(anyString())).thenReturn("");
        when(selectModel.collection.eval(anyObject(), anyObject())).thenReturn(collection);
        
        Collection<Object> result = (Collection<Object>) selectModel.getObjects();
        
        assertEquals(collection, result);
    }
    
    @Test
    public void testGetObjectsWithArrayObjectType() {
        Object[] array = new Object[]{1, 2, 3};
        selectModel.collection = mock(ElExpression.class);
        when(selectModel.collection.errorMessage(anyString())).thenReturn("");
        when(selectModel.collection.eval(anyObject(), anyObject())).thenReturn(array);
        
        Collection<Object> result = (Collection<Object>) selectModel.getObjects();
        
        List<Object> expectedList = new ArrayList<>();
        for (Object obj : array) {
            expectedList.add(obj);
        }
        assertEquals(expectedList, result);
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullResult() {
        selectModel.collection = mock(ElExpression.class);
        when(selectModel.collection.errorMessage(anyString())).thenReturn("");
        when(selectModel.collection.eval(anyObject(), anyObject())).thenReturn(null);
        
        selectModel.getObjects();
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidObjectType() {
        selectModel.collection = mock(ElExpression.class);
        when(selectModel.collection.errorMessage(anyString())).thenReturn("");
        when(selectModel.collection.eval(anyObject(), anyObject())).thenReturn("invalidObject");
        
        selectModel.getObjects();
    }
    
    @Test
    public void testValidateWithValidAttributes() {
        selectModel.collection = mock(ElExpression.class);
        selectModel.label = mock(ElExpression.class);
        selectModel.value = mock(ElExpression.class);
        
        selectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        selectModel.collection = null;
        selectModel.label = mock(ElExpression.class);
        selectModel.value = mock(ElExpression.class);
        
        selectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        selectModel.collection = mock(ElExpression.class);
        selectModel.label = null;
        selectModel.value = mock(ElExpression.class);
        
        selectModel.validate();
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        selectModel.collection = mock(ElExpression.class);
        selectModel.label = mock(ElExpression.class);
        selectModel.value = null;
        
        selectModel.validate();
    }
    
    @Test
    public void testReset() {
        selectModel.objects = mock(Collection.class);
        
        selectModel.reset();
        
        assertNull(selectModel.objects);
    }
}
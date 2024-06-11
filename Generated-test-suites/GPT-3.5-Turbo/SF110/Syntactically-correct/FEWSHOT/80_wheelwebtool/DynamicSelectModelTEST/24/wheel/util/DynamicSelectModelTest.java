import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import wheel.*;
import wheel.components.*;
import wheel.util.DynamicSelectModel;

public class DynamicSelectModelTest {
    
    private DynamicSelectModel dynamicSelectModel;
    
    @Mock
    private Component mockComponent;
    
    @Mock
    private StandaloneComponent mockTopLevelComponent;
    
    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testConstructor() {
        ElExpression mockCollection = mock(ElExpression.class);
        ElExpression mockLabel = mock(ElExpression.class);
        ElExpression mockValue = mock(ElExpression.class);
        
        dynamicSelectModel = new DynamicSelectModel(mockCollection, mockLabel, mockValue);

        assertEquals(mockLabel, dynamicSelectModel.getLabel());
        assertEquals(mockValue, dynamicSelectModel.getValue());
        assertEquals(mockCollection, dynamicSelectModel.getCollection());
    }
    
    @Test
    public void testCollection() {
        String mockCollection = "mockCollection";
        
        dynamicSelectModel.collection(mockCollection);
        
        ElExpression expectedCollection = new ElExpression(mockCollection);
        
        assertEquals(expectedCollection, dynamicSelectModel.getCollection());
    }
    
    @Test
    public void testLabel() {
        String mockLabel = "mockLabel";
        
        dynamicSelectModel.label(mockLabel);
        
        ElExpression expectedLabel = new ElExpression(mockLabel);
        
        assertEquals(expectedLabel, dynamicSelectModel.getLabel());
    }
    
    @Test
    public void testValue() {
        String mockValue = "mockValue";
        
        dynamicSelectModel.value(mockValue);
        
        ElExpression expectedValue = new ElExpression(mockValue);
        
        assertEquals(expectedValue, dynamicSelectModel.getValue());
    }
    
    @Test
    public void testEnumeration() {
        String mockEnumeration = "mockEnumeration";
        
        dynamicSelectModel.enumeration(mockEnumeration);
        
        ElExpression expectedCollection = new ElExpression(mockEnumeration + ".values()");
        ElExpression expectedLabel = new ElExpression("toString()");
        ElExpression expectedValue = new ElExpression("ordinal()");
        
        assertEquals(expectedCollection, dynamicSelectModel.getCollection());
        assertEquals(expectedLabel, dynamicSelectModel.getLabel());
        assertEquals(expectedValue, dynamicSelectModel.getValue());
    }
    
    @Test
    public void testTranslator() {
        String mockTranslator = "mockTranslator";
        
        dynamicSelectModel.translator(mockTranslator);
        
        ElExpression expectedTranslator = new ElExpression(mockTranslator);
        
        assertEquals(expectedTranslator, dynamicSelectModel.getTranslator());
    }
    
    @Test
    public void testEmpty() {
        Object mockObject = mock(Object.class);
        
        dynamicSelectModel.empty(mockObject);
        
        assertEquals(mockObject, dynamicSelectModel.getEmptyObject());
    }
    
    @Test
    public void testGetOptionCount() {
        Collection<Object> mockObjects = mock(Collection.class);
        dynamicSelectModel.setObjects(mockObjects);
        
        int expectedOptionCount = mockObjects.size();
        int actualOptionCount = dynamicSelectModel.getOptionCount();
        
        assertEquals(expectedOptionCount, actualOptionCount);
    }
    
    @Test
    public void testGetLabel() {
        int index = 0;
        String mockLabel = "mockLabel";
        
        List<Object> mockObjects = new ArrayList<Object>();
        Object mockObject = mock(Object.class);
        mockObjects.add(mockObject);
        
        dynamicSelectModel.setObjects(mockObjects);
        dynamicSelectModel.setLabel(new ElExpression(mockLabel));
        
        when(mockObject.toString()).thenReturn(mockLabel);
        
        String expectedLabel = mockLabel;
        String actualLabel = dynamicSelectModel.getLabel(index);
        
        assertEquals(expectedLabel, actualLabel);
    }
    
    @Test
    public void testGetValue() {
        int index = 0;
        String mockValue = "mockValue";
        
        List<Object> mockObjects = new ArrayList<Object>();
        Object mockObject = mock(Object.class);
        mockObjects.add(mockObject);
        
        dynamicSelectModel.setObjects(mockObjects);
        dynamicSelectModel.setValue(new ElExpression(mockValue));
        
        when(mockObject.toString()).thenReturn(mockValue);
        
        String expectedValue = mockValue;
        String actualValue = dynamicSelectModel.getValue(index);
        
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testTranslateValue() {
        String value = "mockValue";
        
        Object mockObject = mock(Object.class);
        Collection<Object> mockObjects = mock(Collection.class);
        
        dynamicSelectModel.setObjects(mockObjects);
        dynamicSelectModel.setTranslator(null);
        
        when(mockObjects.toArray()).thenReturn(new Object[] { mockObject });
        when(dynamicSelectModel.getValue(0)).thenReturn(value);
        
        Object expectedTranslateValue = mockObject;
        Object actualTranslateValue = dynamicSelectModel.translateValue(value);
        
        assertEquals(expectedTranslateValue, actualTranslateValue);
    }
    
    @Test
    public void testTranslateValueWithTranslator() {
        String value = "mockValue";
        
        Object mockObject = mock(Object.class);
        Collection<Object> mockObjects = mock(Collection.class);
        ElExpression mockTranslator = mock(ElExpression.class);
        
        dynamicSelectModel.setObjects(mockObjects);
        dynamicSelectModel.setTranslator(mockTranslator);
        
        when(mockObjects.toArray()).thenReturn(new Object[] { mockObject });
        when(mockTranslator.eval(mockObject, mockComponent)).thenReturn(mockObject);
        
        Object expectedTranslateValue = mockObject;
        Object actualTranslateValue = dynamicSelectModel.translateValue(value);
        
        assertEquals(expectedTranslateValue, actualTranslateValue);
    }
    
    @Test
    public void testGetObjects() {
        Collection<Object> mockObjects = mock(Collection.class);
        dynamicSelectModel.setObjects(mockObjects);
        
        Collection<Object> expectedObjects = mockObjects;
        Collection<Object> actualObjects = dynamicSelectModel.getObjects();
        
        assertEquals(expectedObjects, actualObjects);
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullExpression() {
        dynamicSelectModel.setCollection(null);
        
        try {
            dynamicSelectModel.getObjects();
        } catch (WheelException e) {
            throw e;
        }
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithNullObject() {
        dynamicSelectModel.setCollection(new ElExpression(""));
        dynamicSelectModel.setTopLevelComponent(mockTopLevelComponent);
        dynamicSelectModel.setComponent(mockComponent);
        
        try {
            dynamicSelectModel.getObjects();
        } catch (WheelException e) {
            throw e;
        }
    }
    
    @Test(expected = WheelException.class)
    public void testGetObjectsWithInvalidObject() {
        dynamicSelectModel.setCollection(new ElExpression(""));
        dynamicSelectModel.setTopLevelComponent(mockTopLevelComponent);
        dynamicSelectModel.setComponent(mockComponent);
        
        when(dynamicSelectModel.getObjects().toArray()).thenReturn(new Object[] { "" });
        
        try {
            dynamicSelectModel.getObjects();
        } catch (WheelException e) {
            throw e;
        }
    }
    
    @Test
    public void testReset() {
        Collection<Object> mockObjects = mock(Collection.class);
        dynamicSelectModel.setObjects(mockObjects);
        
        dynamicSelectModel.reset();
        
        assertNull(dynamicSelectModel.getObjects());
    }
    
    @Test
    public void testGetAndSetComponent() {
        dynamicSelectModel.setComponent(mockComponent);
        Component actualComponent = dynamicSelectModel.getComponent();
        
        assertEquals(mockComponent, actualComponent);
    }
    
    @Test
    public void testGetAndSetTopLevelComponent() {
        dynamicSelectModel.setTopLevelComponent(mockTopLevelComponent);
        StandaloneComponent actualTopLevelComponent = dynamicSelectModel.getTopLevelComponent();
        
        assertEquals(mockTopLevelComponent, actualTopLevelComponent);
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        try {
            dynamicSelectModel.validate();
        } catch (WheelException e) {
            throw e;
        }
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        dynamicSelectModel.setCollection(new ElExpression(""));
        
        try {
            dynamicSelectModel.validate();
        } catch (WheelException e) {
            throw e;
        }
    }
    
    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        dynamicSelectModel.setCollection(new ElExpression(""));
        dynamicSelectModel.setLabel(new ElExpression(""));
        
        try {
            dynamicSelectModel.validate();
        } catch (WheelException e) {
            throw e;
        }
    }
    
}
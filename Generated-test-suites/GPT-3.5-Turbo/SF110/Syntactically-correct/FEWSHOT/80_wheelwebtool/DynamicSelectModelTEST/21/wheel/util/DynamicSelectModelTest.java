package wheel.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import wheel.ElExpression;
import wheel.components.Component;
import wheel.components.StandaloneComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;
    private Component mockComponent;
    private StandaloneComponent mockTopLevelComponent;
    
    @Before
    public void setUp() {
        this.dynamicSelectModel = new DynamicSelectModel();
        this.mockComponent = mock(Component.class);
        this.mockTopLevelComponent = mock(StandaloneComponent.class);
        
        dynamicSelectModel.setComponent(mockComponent);
        dynamicSelectModel.setTopLevelComponent(mockTopLevelComponent);
    }

    @Test
    public void testConstructor() {
        ElExpression mockCollection = mock(ElExpression.class);
        ElExpression mockLabel = mock(ElExpression.class);
        ElExpression mockValue = mock(ElExpression.class);

        dynamicSelectModel = new DynamicSelectModel(mockCollection, mockLabel, mockValue);

        verify(mockCollection, times(1)).errorMessage(anyString());
    }

    @Test
    public void testCollection() {
        String collection = "test collection";
        dynamicSelectModel.collection(collection);

        ElExpression expectedCollection = new ElExpression(collection);
        ElExpression actualCollection = dynamicSelectModel.collection;

        assertEquals(expectedCollection, actualCollection);
    }

    @Test
    public void testLabel() {
        String label = "test label";
        dynamicSelectModel.label(label);

        ElExpression expectedLabel = new ElExpression(label);
        ElExpression actualLabel = dynamicSelectModel.label;

        assertEquals(expectedLabel, actualLabel);
    }

    @Test
    public void testValue() {
        String value = "test value";
        dynamicSelectModel.value(value);

        ElExpression expectedValue = new ElExpression(value);
        ElExpression actualValue = dynamicSelectModel.value;

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testEnumeration() {
        String enumeration = "test enumeration";
        dynamicSelectModel.enumeration(enumeration);

        ElExpression expectedCollection = new ElExpression(enumeration + ".values()");
        ElExpression expectedLabel = new ElExpression("toString()");
        ElExpression expectedValue = new ElExpression("ordinal()");

        ElExpression actualCollection = dynamicSelectModel.collection;
        ElExpression actualLabel = dynamicSelectModel.label;
        ElExpression actualValue = dynamicSelectModel.value;

        assertEquals(expectedCollection, actualCollection);
        assertEquals(expectedLabel, actualLabel);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testTranslator() {
        String translator = "test translator";
        dynamicSelectModel.translator(translator);

        ElExpression expectedTranslator = new ElExpression(translator);
        ElExpression actualTranslator = dynamicSelectModel.translator;

        assertEquals(expectedTranslator, actualTranslator);
    }

    @Test
    public void testEmpty() {
        Object emptyObject = new Object();
        dynamicSelectModel.empty(emptyObject);

        Object expectedEmptyObject = emptyObject;
        Object actualEmptyObject = dynamicSelectModel.emptyObject;

        assertEquals(expectedEmptyObject, actualEmptyObject);
    }

    @Test
    public void testGetOptionCount() {
        Collection mockObjects = mock(Collection.class);

        when(mockObjects.size()).thenReturn(2);
        when(dynamicSelectModel.getObjects()).thenReturn(mockObjects);

        int expectedOptionCount = 2;
        int actualOptionCount = dynamicSelectModel.getOptionCount();

        assertEquals(expectedOptionCount, actualOptionCount);
    }

    @Test
    public void testGetLabel() {
        Collection mockObjects = mock(Collection.class);
        Object mockObject = mock(Object.class);

        when(mockObject.toString()).thenReturn("Test Label");
        when(mockObjects.toArray()).thenReturn(new Object[]{mockObject});
        when(mockObjects.size()).thenReturn(1);
        when(dynamicSelectModel.getObjects()).thenReturn(mockObjects);
        when(dynamicSelectModel.getLabel(0)).thenCallRealMethod();

        String expectedLabel = "Test Label";
        String actualLabel = dynamicSelectModel.getLabel(0);

        assertEquals(expectedLabel, actualLabel);
    }

    @Test
    public void testGetValue() {
        Collection mockObjects = mock(Collection.class);
        Object mockObject = mock(Object.class);

        when(mockObject.toString()).thenReturn("Test Value");
        when(mockObjects.toArray()).thenReturn(new Object[]{mockObject});
        when(mockObjects.size()).thenReturn(1);
        when(dynamicSelectModel.getObjects()).thenReturn(mockObjects);
        when(dynamicSelectModel.getValue(0)).thenCallRealMethod();

        String expectedValue = "Test Value";
        String actualValue = dynamicSelectModel.getValue(0);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testTranslateValue() {
        Collection mockObjects = mock(Collection.class);
        Object mockObject = mock(Object.class);

        when(mockObject.toString()).thenReturn("Test Value");
        when(mockObjects.toArray()).thenReturn(new Object[]{mockObject});
        when(mockObjects.size()).thenReturn(1);
        when(dynamicSelectModel.getObjects()).thenReturn(mockObjects);

        Object expectedTranslation = mockObject;
        Object actualTranslation = dynamicSelectModel.translateValue("Test Value");

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    public void testTranslateValue_TranslatorNotNull() {
        Collection mockObjects = mock(Collection.class);
        Object mockObject = mock(Object.class);
        ElExpression mockTranslator = mock(ElExpression.class);

        when(mockObject.toString()).thenReturn("Test Value");
        when(mockObjects.toArray()).thenReturn(new Object[]{mockObject});
        when(mockObjects.size()).thenReturn(1);
        when(dynamicSelectModel.getObjects()).thenReturn(mockObjects);
        when(dynamicSelectModel.translateValue("Test Value")).thenCallRealMethod();
        when(dynamicSelectModel.getValue(0)).thenReturn("Test Value");
        when(dynamicSelectModel.getTranslator()).thenReturn(mockTranslator);
        when(mockTranslator.eval(mockObject, mockComponent)).thenReturn(mockObject);

        Object expectedTranslation = mockObject;
        Object actualTranslation = dynamicSelectModel.translateValue("Test Value");

        assertEquals(expectedTranslation, actualTranslation);
        verify(mockTranslator, times(1)).eval(mockObject, mockComponent);
    }

    @Test
    public void testTranslateValue_ValueNotFound() {
        Collection mockObjects = mock(Collection.class);
        Object mockObject = mock(Object.class);

        when(mockObject.toString()).thenReturn("Test Value");
        when(mockObjects.toArray()).thenReturn(new Object[]{mockObject});
        when(mockObjects.size()).thenReturn(1);
        when(dynamicSelectModel.getObjects()).thenReturn(mockObjects);

        Object expectedTranslation = null;
        Object actualTranslation = dynamicSelectModel.translateValue("Other Value");

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    public void testGetObjects_Collection() {
        Collection mockObjects = mock(Collection.class);

        when(dynamicSelectModel.getObjects()).thenCallRealMethod();
        when(dynamicSelectModel.getObjects()).thenReturn(mockObjects);

        Collection expectedObjects = mockObjects;
        Collection actualObjects = dynamicSelectModel.getObjects();

        assertEquals(expectedObjects, actualObjects);
    }

    @Test
    public void testGetObjects_Array() {
        Object[] mockObjects = new Object[2];

        when(dynamicSelectModel.getObjects()).thenCallRealMethod();
        when(dynamicSelectModel.getObjects()).thenReturn(Arrays.asList(mockObjects));

        Collection expectedObjects = Arrays.asList(mockObjects);
        Collection actualObjects = dynamicSelectModel.getObjects();

        assertEquals(expectedObjects, actualObjects);
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_NullEvaluatedExpression() {
        ElExpression mockExpression = mock(ElExpression.class);

        when(dynamicSelectModel.getObjects()).thenCallRealMethod();
        when(dynamicSelectModel.getObjects()).thenReturn(mock(Collection.class));
        when(mockExpression.eval(mockTopLevelComponent, mockComponent)).thenReturn(null);
        when(dynamicSelectModel.getCollection()).thenReturn(mockExpression);

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_InvalidEvaluatedExpression() {
        ElExpression mockExpression = mock(ElExpression.class);

        when(dynamicSelectModel.getObjects()).thenCallRealMethod();
        when(dynamicSelectModel.getObjects()).thenReturn(mock(Collection.class));
        when(mockExpression.eval(mockTopLevelComponent, mockComponent)).thenReturn(mock(Object.class));
        when(dynamicSelectModel.getCollection()).thenReturn(mockExpression);

        dynamicSelectModel.getObjects();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.objects = new ArrayList<>();

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.objects);
    }
}
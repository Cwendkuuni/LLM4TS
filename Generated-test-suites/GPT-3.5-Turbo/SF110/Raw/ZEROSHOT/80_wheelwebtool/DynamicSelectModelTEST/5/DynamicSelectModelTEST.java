package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wheel.util.DynamicSelectModel;
import wheel.components.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DynamicSelectModelTest {

    @Mock
    Component mockComponent;

    @Mock
    StandaloneComponent mockTopLevelComponent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructorWithArguments() {
        ElExpression mockCollection = mock(ElExpression.class);
        ElExpression mockLabel = mock(ElExpression.class);
        ElExpression mockValue = mock(ElExpression.class);

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(mockCollection, mockLabel, mockValue);

        assertNotNull(dynamicSelectModel.getLabel(0));
        assertNotNull(dynamicSelectModel.getValue(0));
    }

    @Test
    public void testDefaultConstructor() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        assertNotNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testCollection() {
        String collection = "testCollection";
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        DynamicSelectModel result = dynamicSelectModel.collection(collection);

        assertEquals(collection, dynamicSelectModel.collection.getExpression());
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testLabel() {
        String label = "testLabel";
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        DynamicSelectModel result = dynamicSelectModel.label(label);

        assertEquals(label, dynamicSelectModel.label.getExpression());
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testValue() {
        String value = "testValue";
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        DynamicSelectModel result = dynamicSelectModel.value(value);

        assertEquals(value, dynamicSelectModel.value.getExpression());
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testEnumeration() {
        String enumeration = "testEnumeration";
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        DynamicSelectModel result = dynamicSelectModel.enumeration(enumeration);

        assertNotNull(dynamicSelectModel.collection);
        assertNotNull(dynamicSelectModel.label);
        assertNotNull(dynamicSelectModel.value);
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testTranslator() {
        String translator = "testTranslator";
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        DynamicSelectModel result = dynamicSelectModel.translator(translator);

        assertEquals(translator, dynamicSelectModel.translator.getExpression());
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testEmpty() {
        Object o = new Object();
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        DynamicSelectModel result = dynamicSelectModel.empty(o);

        assertEquals(o, dynamicSelectModel.emptyObject);
        assertEquals(dynamicSelectModel, result);
    }

    @Test
    public void testGetOptionCount() {
        Collection mockCollection = mock(Collection.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = mockCollection;

        int result = dynamicSelectModel.getOptionCount();

        assertEquals(mockCollection.size(), result);
    }

    @Test
    public void testGetLabel() {
        int index = 0;
        String expression = "testExpression";
        Object mockObject = mock(Object.class);
        Component mockComponent = mock(Component.class);
        ElExpression mockLabel = mock(ElExpression.class);
        when(mockLabel.eval(anyObject(), anyObject())).thenReturn(mockObject);

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label = mockLabel;
        dynamicSelectModel.component = mockComponent;
        dynamicSelectModel.objects = Arrays.asList(expression);

        String result = dynamicSelectModel.getLabel(index);

        assertEquals(mockObject.toString(), result);
        verify(mockLabel).errorMessage("Could not evaluate expression " + expression + " to create a label for dynamic ISelectModel. Index was " + index);
    }

    @Test
    public void testGetValue() {
        int index = 0;
        String expression = "testExpression";
        Object mockObject = mock(Object.class);
        Component mockComponent = mock(Component.class);
        ElExpression mockValue = mock(ElExpression.class);
        when(mockValue.eval(anyObject(), anyObject())).thenReturn(mockObject);

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.value = mockValue;
        dynamicSelectModel.component = mockComponent;
        dynamicSelectModel.objects = Arrays.asList(expression);

        String result = dynamicSelectModel.getValue(index);

        assertEquals(mockObject.toString(), result);
        verify(mockValue).errorMessage("Could not evaluate expression " + (Object)this.value + " to create a value for dynamic ISelectModel. Index was " + index);
    }

    @Test
    public void testTranslateValue() {
        String expression = "testExpression";
        String value = "testValue";
        Object mockValue = mock(Object.class);
        Component mockComponent = mock(Component.class);
        ElExpression mockTranslator = mock(ElExpression.class);
        when(mockTranslator.eval(anyObject(), anyObject())).thenReturn(mockValue);

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.translator = mockTranslator;
        dynamicSelectModel.component = mockComponent;
        dynamicSelectModel.objects = Arrays.asList(expression, value);

        Object result = dynamicSelectModel.translateValue(value);

        assertEquals(mockValue, result);
        verify(mockTranslator, times(1)).errorMessage("Could not evaluate expression " + (Object)this.translator + " to translate value ' " + value + "'");
    }

    @Test
    public void testTranslateValueWhenTranslatorIsNull() {
        String expression = "testExpression";
        String value = "testValue";
        Component mockComponent = mock(Component.class);

        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.translator = null;
        dynamicSelectModel.component = mockComponent;
        dynamicSelectModel.objects = Arrays.asList(expression, value);

        Object result = dynamicSelectModel.translateValue(value);

        assertNull(result);
    }

    @Test
    public void testGetObjectsWhenObjectsIsNull() {
        Component mockComponent = mock(Component.class);
        StandaloneComponent mockTopLevelComponent = mock(StandaloneComponent.class);
        ElExpression mockCollection = mock(ElExpression.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.component = mockComponent;
        dynamicSelectModel.topLevelComponent = mockTopLevelComponent;
        dynamicSelectModel.collection = mockCollection;

        Object[] array = new Object[]{new Object()};
        when(mockCollection.eval(anyObject(), anyObject())).thenReturn(array);

        Collection result = dynamicSelectModel.getObjects();

        assertNotNull(result);
        assertEquals(Arrays.asList(array), result);
        verify(mockCollection, times(1)).errorMessage("Could not evaluate expression " + (Object)this.collection + " to create a dynamic ISelectModel.");
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWhenObjectIsNull() {
        Component mockComponent = mock(Component.class);
        StandaloneComponent mockTopLevelComponent = mock(StandaloneComponent.class);
        ElExpression mockCollection = mock(ElExpression.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.component = mockComponent;
        dynamicSelectModel.topLevelComponent = mockTopLevelComponent;
        dynamicSelectModel.collection = mockCollection;

        when(mockCollection.eval(anyObject(), anyObject())).thenReturn(null);

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjectsWhenObjectIsInvalid() {
        Component mockComponent = mock(Component.class);
        StandaloneComponent mockTopLevelComponent = mock(StandaloneComponent.class);
        ElExpression mockCollection = mock(ElExpression.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.component = mockComponent;
        dynamicSelectModel.topLevelComponent = mockTopLevelComponent;
        dynamicSelectModel.collection = mockCollection;

        when(mockCollection.eval(anyObject(), anyObject())).thenReturn(new Object());

        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetObjectsWhenCollectionIsNotNullAndEmptyObjectIsNull() {
        Collection mockCollection = mock(Collection.class);
        Component mockComponent = mock(Component.class);
        StandaloneComponent mockTopLevelComponent = mock(StandaloneComponent.class);
        ElExpression mockCollectionElExpression = mock(ElExpression.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.component = mockComponent;
        dynamicSelectModel.topLevelComponent = mockTopLevelComponent;
        dynamicSelectModel.collection = mockCollectionElExpression;
        dynamicSelectModel.objects = mockCollection;

        Collection result = dynamicSelectModel.getObjects();

        assertEquals(mockCollection, result);
    }

    @Test
    public void testGetObjectsWhenEmptyObjectIsNotNull() {
        Object mockEmptyObject = mock(Object.class);
        Collection mockCollection = mock(Collection.class);
        Component mockComponent = mock(Component.class);
        StandaloneComponent mockTopLevelComponent = mock(StandaloneComponent.class);
        ElExpression mockCollectionElExpression = mock(ElExpression.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.component = mockComponent;
        dynamicSelectModel.topLevelComponent = mockTopLevelComponent;
        dynamicSelectModel.collection = mockCollectionElExpression;
        dynamicSelectModel.objects = mockCollection;
        dynamicSelectModel.emptyObject = mockEmptyObject;

        Collection result = dynamicSelectModel.getObjects();

        assertNotNull(result);
        assertTrue(result.contains(mockEmptyObject));
    }

    @Test
    public void testGetObjectsWhenEmptyObjectIsNull() {
        Collection mockCollection = mock(Collection.class);
        Component mockComponent = mock(Component.class);
        StandaloneComponent mockTopLevelComponent = mock(StandaloneComponent.class);
        ElExpression mockCollectionElExpression = mock(ElExpression.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.component = mockComponent;
        dynamicSelectModel.topLevelComponent = mockTopLevelComponent;
        dynamicSelectModel.collection = mockCollectionElExpression;
        dynamicSelectModel.objects = mockCollection;
        dynamicSelectModel.emptyObject = null;

        Collection result = dynamicSelectModel.getObjects();

        assertNotNull(result);
    }

    @Test
    public void testGetComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.component = mockComponent;

        Component result = dynamicSelectModel.getComponent();

        assertEquals(mockComponent, result);
    }

    @Test
    public void testSetComponent() {
        Component component = mock(Component.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.setComponent(component);

        assertEquals(component, dynamicSelectModel.component);
    }

    @Test
    public void testGetTopLevelComponent() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.topLevelComponent = mockTopLevelComponent;

        StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();

        assertEquals(mockTopLevelComponent, result);
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent topLevelComponent = mock(StandaloneComponent.class);
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

        dynamicSelectModel.setTopLevelComponent(topLevelComponent);

        assertEquals(topLevelComponent, dynamicSelectModel.topLevelComponent);
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullCollection() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.label = mock(ElExpression.class);
        dynamicSelectModel.value = mock(ElExpression.class);

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullLabel() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = mock(ElExpression.class);
        dynamicSelectModel.value = mock(ElExpression.class);

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidateWithNullValue() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.collection = mock(ElExpression.class);
        dynamicSelectModel.label = mock(ElExpression.class);

        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.objects = mock(Collection.class);

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.objects);
    }
}
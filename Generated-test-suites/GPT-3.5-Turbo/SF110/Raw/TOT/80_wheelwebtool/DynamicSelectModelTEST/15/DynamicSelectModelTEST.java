import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.ElExpression;
import wheel.WheelException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class DynamicSelectModelTest {

    @Mock
    private Component mockComponent;
    @Mock
    private StandaloneComponent mockTopLevelComponent;

    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dynamicSelectModel = new DynamicSelectModel();
        dynamicSelectModel.setComponent(mockComponent);
        dynamicSelectModel.setTopLevelComponent(mockTopLevelComponent);
    }

    @Test
    public void testConstructor_withCollectionLabelValue_Valid() {
        ElExpression mockCollection = mock(ElExpression.class);
        ElExpression mockLabel = mock(ElExpression.class);
        ElExpression mockValue = mock(ElExpression.class);
        when(mockCollection.getExpression()).thenReturn("testCollection");
        when(mockLabel.getExpression()).thenReturn("testLabel");
        when(mockValue.getExpression()).thenReturn("testValue");

        dynamicSelectModel = new DynamicSelectModel(mockCollection, mockLabel, mockValue);

        assertNotNull(dynamicSelectModel);
        assertEquals(mockCollection, dynamicSelectModel.collection);
        assertEquals(mockLabel, dynamicSelectModel.label);
        assertEquals(mockValue, dynamicSelectModel.value);
    }

    @Test(expected = WheelException.class)
    public void testConstructor_withNullCollection() {
        ElExpression mockLabel = mock(ElExpression.class);
        ElExpression mockValue = mock(ElExpression.class);

        dynamicSelectModel = new DynamicSelectModel(null, mockLabel, mockValue);
    }

    @Test(expected = WheelException.class)
    public void testConstructor_withNullLabel() {
        ElExpression mockCollection = mock(ElExpression.class);
        ElExpression mockValue = mock(ElExpression.class);

        dynamicSelectModel = new DynamicSelectModel(mockCollection, null, mockValue);
    }

    @Test(expected = WheelException.class)
    public void testConstructor_withNullValue() {
        ElExpression mockCollection = mock(ElExpression.class);
        ElExpression mockLabel = mock(ElExpression.class);

        dynamicSelectModel = new DynamicSelectModel(mockCollection, mockLabel, null);
    }

    @Test
    public void testCollection() {
        String testCollection = "testCollection";

        dynamicSelectModel.collection(testCollection);

        assertEquals(new ElExpression(testCollection), dynamicSelectModel.collection);
    }

    @Test
    public void testLabel() {
        String testLabel = "testLabel";

        dynamicSelectModel.label(testLabel);

        assertEquals(new ElExpression(testLabel), dynamicSelectModel.label);
    }

    @Test
    public void testValue() {
        String testValue = "testValue";

        dynamicSelectModel.value(testValue);

        assertEquals(new ElExpression(testValue), dynamicSelectModel.value);
    }

    @Test
    public void testEnumeration() {
        String testEnumeration = "testEnumeration";

        dynamicSelectModel.enumeration(testEnumeration);

        assertEquals(new ElExpression(testEnumeration + ".values()"), dynamicSelectModel.collection);
        assertEquals(new ElExpression("toString()"), dynamicSelectModel.label);
        assertEquals(new ElExpression("ordinal()"), dynamicSelectModel.value);
    }

    @Test
    public void testTranslator() {
        String testTranslator = "testTranslator";

        dynamicSelectModel.translator(testTranslator);

        assertEquals(new ElExpression(testTranslator), dynamicSelectModel.translator);
    }

    @Test
    public void testEmpty() {
        Object testObject = new Object();

        dynamicSelectModel.empty(testObject);

        assertEquals(testObject, dynamicSelectModel.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        Collection<Object> testObjects = new ArrayList<>();
        testObjects.add("Option 1");
        testObjects.add("Option 2");
        dynamicSelectModel.objects = testObjects;

        int expectedOptionCount = 2;
        int actualOptionCount = dynamicSelectModel.getOptionCount();

        assertEquals(expectedOptionCount, actualOptionCount);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionCount_withNullObjects() {
        dynamicSelectModel.getOptionCount();
    }

    @Test
    public void testGetLabel() {
        Collection<Object> testObjects = new ArrayList<>();
        testObjects.add("Option 1");
        testObjects.add("Option 2");
        dynamicSelectModel.objects = testObjects;

        String expectedLabel = "Option 2";
        String actualLabel = dynamicSelectModel.getLabel(1);

        assertEquals(expectedLabel, actualLabel);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLabel_withNullObjects() {
        dynamicSelectModel.getLabel(0);
    }

    @Test
    public void testGetValue() {
        Collection<Object> testObjects = new ArrayList<>();
        testObjects.add("Option 1");
        testObjects.add("Option 2");
        dynamicSelectModel.objects = testObjects;

        String expectedValue = "Option 1";
        String actualValue = dynamicSelectModel.getValue(0);

        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = NullPointerException.class)
    public void testGetValue_withNullObjects() {
        dynamicSelectModel.getValue(0);
    }

    @Test
    public void testTranslateValue_withTranslator() {
        String testValue = "Option 1";
        dynamicSelectModel.translator = new ElExpression("translatorExpression");
        Object expectedResult = new Object();

        ElExpression mockTranslatorExpression = mock(ElExpression.class);
        when(dynamicSelectModel.getValue(anyInt())).thenReturn(testValue);
        when(mockTranslatorExpression.getExpression()).thenReturn("translatorExpression");
        when(mockTranslatorExpression.eval(any(), any())).thenReturn(expectedResult);

        Object actualResult = dynamicSelectModel.translateValue(testValue);

        assertEquals(expectedResult, actualResult);
        verify(mockTranslatorExpression, times(1)).eval(any(), any());
    }

    @Test
    public void testTranslateValue_withoutTranslator() {
        String testValue = "Option 1";
        dynamicSelectModel.translator = null;
        Collection<Object> testObjects = new ArrayList<>();
        testObjects.add("Option 1");

        dynamicSelectModel.objects = testObjects;

        Object expectedResult = "Option 1";
        Object actualResult = dynamicSelectModel.translateValue(testValue);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testTranslateValue_withValueNotFound() {
        String testValue = "Option 3";
        dynamicSelectModel.translator = new ElExpression("translatorExpression");
        Collection<Object> testObjects = new ArrayList<>();
        testObjects.add("Option 1");
        testObjects.add("Option 2");

        dynamicSelectModel.objects = testObjects;

        Object actualResult = dynamicSelectModel.translateValue(testValue);

        assertNull(actualResult);
    }

    @Test
    public void testGetObjects_withObjectsCollection() {
        Collection<Object> testObjects = new ArrayList<>();
        testObjects.add("Option 1");
        testObjects.add("Option 2");

        dynamicSelectModel.objects = testObjects;

        Collection<Object> actualObjects = dynamicSelectModel.getObjects();

        assertEquals(testObjects, actualObjects);
    }

    @Test
    public void testGetObjects_withNullObjects() {
        ElExpression mockCollection = mock(ElExpression.class);
        when(mockCollection.getExpression()).thenReturn("testCollection");
        when(mockCollection.eval(mockTopLevelComponent, mockComponent)).thenReturn(new Object[0]);

        dynamicSelectModel.collection = mockCollection;

        Collection<Object> actualObjects = dynamicSelectModel.getObjects();

        assertNotNull(actualObjects);
        assertTrue(actualObjects.isEmpty());
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_withNullResult() {
        ElExpression mockCollection = mock(ElExpression.class);
        when(mockCollection.getExpression()).thenReturn("testCollection");
        when(mockCollection.eval(mockTopLevelComponent, mockComponent)).thenReturn(null);

        dynamicSelectModel.collection = mockCollection;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_withInvalidResultType() {
        ElExpression mockCollection = mock(ElExpression.class);
        when(mockCollection.getExpression()).thenReturn("testCollection");
        when(mockCollection.eval(mockTopLevelComponent, mockComponent)).thenReturn(new Object());

        dynamicSelectModel.collection = mockCollection;

        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_withInvalidResultTypeArray() {
        ElExpression mockCollection = mock(ElExpression.class);
        when(mockCollection.getExpression()).thenReturn("testCollection");
        when(mockCollection.eval(mockTopLevelComponent, mockComponent)).thenReturn(new Object[0]);

        dynamicSelectModel.collection = mockCollection;

        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetObjects_withEmptyObject() {
        ElExpression mockCollection = mock(ElExpression.class);
        when(mockCollection.getExpression()).thenReturn("testCollection");
        when(mockCollection.eval(mockTopLevelComponent, mockComponent)).thenReturn(new Object[0]);

        dynamicSelectModel.collection = mockCollection;
        dynamicSelectModel.emptyObject = "Empty Option";

        Collection<Object> actualObjects = dynamicSelectModel.getObjects();

        assertNotNull(actualObjects);
        assertTrue(actualObjects.contains("Empty Option"));
    }

    @Test(expected = WheelException.class)
    public void testValidate_withNullCollection() {
        dynamicSelectModel.collection = null;

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_withNullLabel() {
        dynamicSelectModel.label = null;

        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_withNullValue() {
        dynamicSelectModel.value = null;

        dynamicSelectModel.validate();
    }

    @Test
    public void testReset() {
        dynamicSelectModel.objects = new ArrayList<>();

        dynamicSelectModel.reset();

        assertNull(dynamicSelectModel.objects);
    }
}
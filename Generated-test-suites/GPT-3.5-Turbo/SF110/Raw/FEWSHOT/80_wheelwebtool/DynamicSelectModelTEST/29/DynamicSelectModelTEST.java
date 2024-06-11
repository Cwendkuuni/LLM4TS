import wheel.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setUp() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test(expected = WheelException.class)
    public void testValidate_collectionIsNull() {
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_labelIsNull() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.validate();
    }

    @Test(expected = WheelException.class)
    public void testValidate_valueIsNull() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.label("label");
        dynamicSelectModel.validate();
    }

    @Test
    public void testConstructor_collectionLabelValue() {
        dynamicSelectModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
        assertNotNull(dynamicSelectModel);
        assertEquals("collection", dynamicSelectModel.collection.getExpression());
        assertEquals("label", dynamicSelectModel.label.getExpression());
        assertEquals("value", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testConstructor_empty() {
        dynamicSelectModel = new DynamicSelectModel();
        assertNotNull(dynamicSelectModel);
    }

    @Test
    public void testCollection() {
        dynamicSelectModel.collection("collection");
        assertEquals("collection", dynamicSelectModel.collection.getExpression());
    }

    @Test
    public void testCollection_chained() {
        dynamicSelectModel.collection("collection").collection("newCollection");
        assertEquals("newCollection", dynamicSelectModel.collection.getExpression());
    }

    @Test
    public void testLabel() {
        dynamicSelectModel.label("label");
        assertEquals("label", dynamicSelectModel.label.getExpression());
    }

    @Test
    public void testLabel_chained() {
        dynamicSelectModel.label("label").label("newLabel");
        assertEquals("newLabel", dynamicSelectModel.label.getExpression());
    }

    @Test
    public void testValue() {
        dynamicSelectModel.value("value");
        assertEquals("value", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testValue_chained() {
        dynamicSelectModel.value("value").value("newValue");
        assertEquals("newValue", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testEnumeration() {
        dynamicSelectModel.enumeration("enumeration");
        assertEquals("enumeration.values()", dynamicSelectModel.collection.getExpression());
        assertEquals("toString()", dynamicSelectModel.label.getExpression());
        assertEquals("ordinal()", dynamicSelectModel.value.getExpression());
    }

    @Test
    public void testTranslator() {
        dynamicSelectModel.translator("translator");
        assertEquals("translator", dynamicSelectModel.translator.getExpression());
    }

    @Test
    public void testEmpty() {
        dynamicSelectModel.empty("emptyObject");
        assertEquals("emptyObject", dynamicSelectModel.emptyObject);
    }

    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add(new Object());
        assertEquals(1, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.label = new ElExpression("label");
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.objects.add("object2");
        assertEquals("object1", dynamicSelectModel.getLabel(0));
        assertEquals("object2", dynamicSelectModel.getLabel(1));
    }

    @Test(expected = WheelException.class)
    public void testGetLabel_indexOutOfRange() {
        dynamicSelectModel.label = new ElExpression("label");
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.getLabel(2);
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.value = new ElExpression("value");
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.objects.add("object2");
        assertEquals("object1", dynamicSelectModel.getValue(0));
        assertEquals("object2", dynamicSelectModel.getValue(1));
    }

    @Test(expected = WheelException.class)
    public void testGetValue_indexOutOfRange() {
        dynamicSelectModel.value = new ElExpression("value");
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.getValue(2);
    }

    @Test
    public void testTranslateValue_translatorNotNull() {
        dynamicSelectModel.translator = new ElExpression("translator");
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.component = new Component();
        assertNotNull(dynamicSelectModel.translateValue("value"));
    }

    @Test
    public void testTranslateValue_translatorNull() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.component = new Component();
        assertNotNull(dynamicSelectModel.translateValue("value"));
    }

    @Test
    public void testGetObjects_objectsNull() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        assertNotNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testGetObjects_objectsNotNull() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        assertNotNull(dynamicSelectModel.getObjects());
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_nullEvaluation() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_expressionNotCollection() {
        dynamicSelectModel.collection = new ElExpression("object");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.getObjects();
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_expressionNotArray() {
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        dynamicSelectModel.getObjects();
    }

    @Test
    public void testGetObjects_newCollectionNotNull() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.emptyObject = "emptyObject";
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        assertNotNull(dynamicSelectModel.getObjects());
    }

    @Test
    public void testGetObjects_newCollectionNotNull_size() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.emptyObject = "emptyObject";
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        assertEquals(2, dynamicSelectModel.getObjects().size());
    }

    @Test
    public void testGetObjects_newCollectionNotNull_order() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.emptyObject = "emptyObject";
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        List<Object> objects = new ArrayList<>();
        objects.add("emptyObject");
        objects.add("object1");
        assertEquals(objects, dynamicSelectModel.getObjects());
    }

    @Test
    public void testGetObjects_noEmptyObject() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.emptyObject = null;
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        assertEquals(dynamicSelectModel.objects, dynamicSelectModel.getObjects());
    }

    @Test
    public void testGetObjects_emptyObjectNull() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.objects.add("object1");
        dynamicSelectModel.collection = new ElExpression("collection");
        dynamicSelectModel.emptyObject = null;
        dynamicSelectModel.topLevelComponent = new StandaloneComponent();
        dynamicSelectModel.component = new Component();
        assertEquals(dynamicSelectModel.objects, dynamicSelectModel.getObjects());
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
        assertEquals(component, dynamicSelectModel.component);
    }

    @Test
    public void testGetTopLevelComponent() {
        StandaloneComponent standaloneComponent = new StandaloneComponent();
        dynamicSelectModel.topLevelComponent = standaloneComponent;
        assertEquals(standaloneComponent, dynamicSelectModel.getTopLevelComponent());
    }

    @Test
    public void testSetTopLevelComponent() {
        StandaloneComponent standaloneComponent = new StandaloneComponent();
        dynamicSelectModel.setTopLevelComponent(standaloneComponent);
        assertEquals(standaloneComponent, dynamicSelectModel.topLevelComponent);
    }

    @Test
    public void testReset() {
        dynamicSelectModel.objects = new ArrayList<>();
        dynamicSelectModel.reset();
        assertNull(dynamicSelectModel.objects);
    }
}
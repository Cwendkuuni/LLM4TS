package wheel.util;

import wheel.components.*;
import wheel.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {
    private DynamicSelectModel dynamicSelectModel;

    @Before
    public void setup() {
        dynamicSelectModel = new DynamicSelectModel();
    }

    @Test
    public void testGetOptionCount() {
        dynamicSelectModel.collection("collection");
        assertEquals(4, dynamicSelectModel.getOptionCount());

        dynamicSelectModel.collection("nullCollection");
        assertEquals(0, dynamicSelectModel.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.label("label");
        assertEquals("obj1", dynamicSelectModel.getLabel(0));
        assertEquals("obj2", dynamicSelectModel.getLabel(1));
        assertEquals("obj3", dynamicSelectModel.getLabel(2));
        assertEquals("obj4", dynamicSelectModel.getLabel(3));

        dynamicSelectModel.collection("emptyCollection");
        assertEquals(null, dynamicSelectModel.getLabel(0));
    }

    @Test
    public void testGetValue() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.value("value");
        assertEquals("value1", dynamicSelectModel.getValue(0));
        assertEquals("value2", dynamicSelectModel.getValue(1));
        assertEquals("value3", dynamicSelectModel.getValue(2));
        assertEquals("value4", dynamicSelectModel.getValue(3));

        dynamicSelectModel.collection("nullCollection");
        assertEquals(null, dynamicSelectModel.getValue(0));
    }

    @Test
    public void testTranslateValue() {
        dynamicSelectModel.collection("collection");
        dynamicSelectModel.value("value");
        dynamicSelectModel.translator("translator");
        assertEquals("obj1", dynamicSelectModel.translateValue("value1"));
        assertEquals("obj2", dynamicSelectModel.translateValue("value2"));
        assertEquals("obj3", dynamicSelectModel.translateValue("value3"));
        assertEquals("obj4", dynamicSelectModel.translateValue("value4"));

        dynamicSelectModel.translator(null);
        assertEquals("obj1", dynamicSelectModel.translateValue("value1"));
        assertEquals("obj2", dynamicSelectModel.translateValue("value2"));
        assertEquals("obj3", dynamicSelectModel.translateValue("value3"));
        assertEquals("obj4", dynamicSelectModel.translateValue("value4"));
    }

    @Test
    public void testGetObjects() {
        dynamicSelectModel.collection("collection");
        Collection objects = dynamicSelectModel.getObjects();
        assertEquals("obj1", objects.toArray()[0].toString());
        assertEquals("obj2", objects.toArray()[1].toString());
        assertEquals("obj3", objects.toArray()[2].toString());
        assertEquals("obj4", objects.toArray()[3].toString());

        dynamicSelectModel.collection("emptyCollection");
        objects = dynamicSelectModel.getObjects();
        assertEquals(null, objects);

        dynamicSelectModel.collection("nullCollection");
        objects = dynamicSelectModel.getObjects();
        assertEquals(null, objects);
    }
}
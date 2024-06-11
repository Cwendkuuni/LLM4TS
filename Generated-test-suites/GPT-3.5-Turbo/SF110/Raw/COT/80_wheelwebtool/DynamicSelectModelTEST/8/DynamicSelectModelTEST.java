
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;


public class DynamicSelectModelTest {
  private DynamicSelectModel dynamicSelectModel;

  @Before
  public void setUp(){
    dynamicSelectModel = new DynamicSelectModel();
  }

  @Test
  public void testConstructor(){
    ElExpression collection = new ElExpression("collection");
    ElExpression label = new ElExpression("label");
    ElExpression value = new ElExpression("value");

    DynamicSelectModel testModel = new DynamicSelectModel(collection, label, value);

    assertNotNull(testModel);
    assertEquals(collection, testModel.getCollection());
    assertEquals(label, testModel.getLabel());
    assertEquals(value, testModel.getValue());
  }

  @Test
  public void testCollection(){
    String collection = "collection";

    DynamicSelectModel testModel = dynamicSelectModel.collection(collection);

    assertNotNull(testModel);
    assertEquals(collection, testModel.getCollection().getExpression());
  }

  @Test
  public void testLabel(){
    String label = "label";

    DynamicSelectModel testModel = dynamicSelectModel.label(label);

    assertNotNull(testModel);
    assertEquals(label, testModel.getLabel().getExpression());
  }

  @Test
  public void testValue(){
    String value = "value";

    DynamicSelectModel testModel = dynamicSelectModel.value(value);

    assertNotNull(testModel);
    assertEquals(value, testModel.getValue().getExpression());
  }

  @Test
  public void testEnumeration(){
    String enumeration = "enumeration";

    DynamicSelectModel testModel = dynamicSelectModel.enumeration(enumeration);

    assertNotNull(testModel);
    assertEquals(enumeration + ".values()", testModel.getCollection().getExpression());
    assertEquals("toString()", testModel.getLabel().getExpression());
    assertEquals("ordinal()", testModel.getValue().getExpression());
  }

  @Test
  public void testTranslator(){
    String translator = "translator";

    DynamicSelectModel testModel = dynamicSelectModel.translator(translator);

    assertNotNull(testModel);
    assertEquals(translator, testModel.getTranslator().getExpression());
  }

  @Test
  public void testEmpty(){
    Object emptyObject = new Object();

    DynamicSelectModel testModel = dynamicSelectModel.empty(emptyObject);

    assertNotNull(testModel);
    assertEquals(emptyObject, testModel.getEmptyObject());
  }

  @Test
  public void testGetOptionCount(){
    Collection<Object> objects = new ArrayList<>();
    objects.add("option1");
    objects.add("option2");
    objects.add("option3");

    dynamicSelectModel.setObjects(objects);

    assertEquals(objects.size(), dynamicSelectModel.getOptionCount());
  }

  @Test
  public void testGetLabel(){
    Collection<Object> objects = new ArrayList<>();
    objects.add("option1");
    objects.add("option2");
    objects.add("option3");

    dynamicSelectModel.setObjects(objects);

    assertEquals("option1", dynamicSelectModel.getLabel(0));
    assertEquals("option2", dynamicSelectModel.getLabel(1));
    assertEquals("option3", dynamicSelectModel.getLabel(2));
  }

  @Test
  public void testGetValue(){
    Collection<Object> objects = new ArrayList<>();
    objects.add("option1");
    objects.add("option2");
    objects.add("option3");

    dynamicSelectModel.setObjects(objects);

    assertEquals("option1", dynamicSelectModel.getValue(0));
    assertEquals("option2", dynamicSelectModel.getValue(1));
    assertEquals("option3", dynamicSelectModel.getValue(2));
  }

  @Test
  public void testTranslateValue(){
    Collection<Object> objects = new ArrayList<>();
    objects.add("option1");
    objects.add("option2");
    objects.add("option3");

    dynamicSelectModel.setObjects(objects);

    assertEquals(objects.toArray()[0], dynamicSelectModel.translateValue("option1"));
    assertEquals(objects.toArray()[1], dynamicSelectModel.translateValue("option2"));
    assertEquals(objects.toArray()[2], dynamicSelectModel.translateValue("option3"));
  }

  @Test
  public void testGetObjects(){
    Collection<Object> objects = new ArrayList<>();
    objects.add("option1");
    objects.add("option2");
    objects.add("option3");

    dynamicSelectModel.setObjects(objects);

    assertEquals(objects, dynamicSelectModel.getObjects());
  }

  @Test
  public void testGetComponent(){
    Component component = new Component();

    dynamicSelectModel.setComponent(component);

    assertEquals(component, dynamicSelectModel.getComponent());
  }

  @Test
  public void testSetComponent(){
    Component component = new Component();

    dynamicSelectModel.setComponent(component);

    assertEquals(component, dynamicSelectModel.getComponent());
  }

  @Test
  public void testGetTopLevelComponent(){
    StandaloneComponent topLevelComponent = new StandaloneComponent();

    dynamicSelectModel.setTopLevelComponent(topLevelComponent);

    assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
  }

  @Test
  public void testSetTopLevelComponent(){
    StandaloneComponent topLevelComponent = new StandaloneComponent();

    dynamicSelectModel.setTopLevelComponent(topLevelComponent);

    assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
  }

  @Test(expected = WheelException.class)
  public void testValidate_NullCollection(){
    dynamicSelectModel.validate();
  }

  @Test(expected = WheelException.class)
  public void testValidate_NullLabel(){
    dynamicSelectModel.setCollection(new ElExpression("collection"));
    dynamicSelectModel.validate();
  }

  @Test(expected = WheelException.class)
  public void testValidate_NullValue(){
    dynamicSelectModel.setCollection(new ElExpression("collection"));
    dynamicSelectModel.setLabel(new ElExpression("label"));
    dynamicSelectModel.validate();
  }

  @Test(expected = WheelException.class)
  public void testGetObjects_NullCollection(){
    dynamicSelectModel.getObjects();
  }

  @Test(expected = WheelException.class)
  public void testGetObjects_NullObject(){
    dynamicSelectModel.setCollection(new ElExpression("collection"));
    dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
    dynamicSelectModel.getObjects();
  }

  @Test(expected = WheelException.class)
  public void testGetObjects_InvalidObjectType(){
    dynamicSelectModel.setCollection(new ElExpression("collection"));
    dynamicSelectModel.setTopLevelComponent(new StandaloneComponent());
    dynamicSelectModel.setObjects(Arrays.asList(1, 2, 3));
    dynamicSelectModel.getObjects();
  }

  @Test
  public void testReset(){
    Collection<Object> objects = new ArrayList<>();
    objects.add("option1");
    objects.add("option2");
    objects.add("option3");

    dynamicSelectModel.setObjects(objects);

    assertNotNull(dynamicSelectModel.getObjects());

    dynamicSelectModel.reset();

    assertNull(dynamicSelectModel.getObjects());
  }
}


import static org.junit.Assert.*;
import org.junit.Test;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;
import wheel.ElExpression;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

public class DynamicSelectModelTest {

  @Test
  public void testGetOptionCount() {
    DynamicSelectModel selectModel = new DynamicSelectModel();
    selectModel.setObjects(new ArrayList<>());
    assertEquals(0, selectModel.getOptionCount());

    selectModel.setObjects(Arrays.asList("Option1", "Option2", "Option3"));
    assertEquals(3, selectModel.getOptionCount());
  }

  @Test
  public void testGetLabel() {
    DynamicSelectModel selectModel = new DynamicSelectModel();
    Object[] objects = {"Option1", "Option2", "Option3"};
    selectModel.setObjects(Arrays.asList(objects));

    selectModel.setLabel("label");
    selectModel.setComponent(new Component());
    assertEquals("Option1", selectModel.getLabel(0));
    assertEquals("Option2", selectModel.getLabel(1));
    assertEquals("Option3", selectModel.getLabel(2));
  }

  @Test
  public void testGetValue() {
    DynamicSelectModel selectModel = new DynamicSelectModel();
    Object[] objects = {"Option1", "Option2", "Option3"};
    selectModel.setObjects(Arrays.asList(objects));

    selectModel.setValue("value");
    selectModel.setComponent(new Component());
    assertEquals("Option1", selectModel.getValue(0));
    assertEquals("Option2", selectModel.getValue(1));
    assertEquals("Option3", selectModel.getValue(2));
  }

  @Test
  public void testTranslateValue() {
    DynamicSelectModel selectModel = new DynamicSelectModel();
    Object[] objects = {"Option1", "Option2", "Option3"};
    selectModel.setObjects(Arrays.asList(objects));

    selectModel.setTranslator("translator");
    selectModel.setComponent(new Component());
    assertEquals("Option1", selectModel.translateValue("Option1"));
    assertEquals("Option2", selectModel.translateValue("Option2"));
    assertEquals("Option3", selectModel.translateValue("Option3"));
  }

  @Test
  public void testGetObjects() {
    DynamicSelectModel selectModel = new DynamicSelectModel();
    Object[] objects = {"Option1", "Option2", "Option3"};
    selectModel.setObjects(Arrays.asList(objects));

    assertEquals(objects, selectModel.getObjects());
  }
}
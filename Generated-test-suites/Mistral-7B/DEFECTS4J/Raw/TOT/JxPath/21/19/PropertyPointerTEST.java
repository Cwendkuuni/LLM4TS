import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.PropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.DummyBean;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyPointerTest {

 private JXPathContext createContext(Object root) {
 return new JXPathContext(root);
 }

 @Test
 public void testPropertyPointerCreation() {
 DummyBean bean = new DummyBean();
 PropertyPointer pointer = new DummyPropertyPointer(new DummyPropertyPointer(null));
 pointer.setPropertyName("name");
 pointer.setPropertyIndex(0);

 assertEquals("name", pointer.getPropertyName());
 assertEquals(0, pointer.getPropertyIndex());
 assertEquals(bean, pointer.getBean());
 }

 @Test
 public void testPropertyPointerGetImmediateNode() {
 DummyBean bean = new DummyBean();
 bean.setName("test");
 PropertyPointer pointer = new DummyPropertyPointer(new DummyPropertyPointer(null));
 pointer.setPropertyName("name");
 pointer.setPropertyIndex(0);
 pointer.setBean(bean);

 assertEquals("test", pointer.getImmediateNode());
 }

 @Test
 public void testPropertyPointerIsCollection() {
 DummyBean bean = new DummyBean();
 bean.setList(new String[]{"test1", "test2"});
 PropertyPointer pointer = new DummyPropertyPointer(new DummyPropertyPointer(null));
 pointer.setPropertyName("list");
 pointer.setBean(bean);

 assertTrue(pointer.isCollection());
 }

 @Test
 public void testPropertyPointerIsLeaf() {
 DummyBean bean = new DummyBean();
 bean.setName("test");
 PropertyPointer pointer = new DummyPropertyPointer(new DummyPropertyPointer(null));
 pointer.setPropertyName("name");
 pointer.setBean(bean);
 pointer.setPropertyIndex(-1);

 assertTrue(pointer.isLeaf());
 }

 @Test
 public void testPropertyPointerGetLength() {
 DummyBean bean = new DummyBean();
 bean.setList(new String[]{"test1", "test2", "test3"});
 PropertyPointer pointer = new DummyPropertyPointer(new DummyPropertyPointer(null));
 pointer.setPropertyName("list");
 pointer.setBean(bean);

 assertEquals(3, pointer.getLength());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPropertyPointerOutOfBoundsIndex() {
 DummyBean bean = new DummyBean();
 bean.setList(new String[]{"test1", "test2"});
 PropertyPointer pointer = new DummyPropertyPointer(new DummyPropertyPointer(null));
 pointer.setPropertyName("list");
 pointer.setBean(bean);
 pointer.setPropertyIndex(2);

 pointer.getImmediateNode();
 }

 @Test
 public void testPropertyPointerCreatePath() {
 DummyBean bean = new DummyBean();
 bean.setName("test");
 PropertyPointer pointer = new DummyPropertyPointer(new DummyPropertyPointer(null));
 pointer.setPropertyName("name");
 pointer.setPropertyIndex(0);
 pointer.setBean(bean);

 JXPathContext context = createContext(bean);
 NodePointer result = pointer.createPath(context);

 assertEquals(bean.getName(), result.getValue());
 }

 @Test
 public void testPropertyPointerCreatePathWithValue() {
 DummyBean bean = new DummyBean();
 PropertyPointer pointer = new DummyPropertyPointer(new DummyPropertyPointer(null));
 pointer.setPropertyName("name");
 pointer.setBean(bean);

 JXPathContext context = createContext(bean);
 pointer.createPath(context, "newName");
 assertEquals("newName", bean.getName());
 }

 @Test
 public void testPropertyPointerCreateChild() {
 DummyBean bean = new DummyBean();
 bean.setName("test");
 bean.setList(new String[]{"test1", "test2"});
 PropertyPointer parentPointer = new DummyPropertyPointer(new DummyPropertyPointer(null));
 parentPointer.setPropertyName("list");
 parentPointer.setBean(bean);
 PropertyPointer childPointer = new DummyPropertyPointer(parentPointer);
 childPointer.setPropertyName("name");
 childPointer.setPropertyIndex(0);

 JXPathContext context = createContext(bean);
 NodePointer child = childPointer.createPath(context, "newName");
 assertEquals("newName", bean.getList()[0]);
 }
}
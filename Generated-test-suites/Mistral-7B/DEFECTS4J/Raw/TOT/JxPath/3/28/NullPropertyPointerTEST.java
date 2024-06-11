import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullPropertyPointerTest {

 @Test
 public void testNullPropertyPointerCreation() {
 NodePointer parent = new NodePointer(new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 assertEquals("root", nullPropertyPointer.getImmediateParentPointer().getName().getLocalPart());
 assertNull(nullPropertyPointer.getValuePointer());
 assertFalse(nullPropertyPointer.isActual());
 assertFalse(nullPropertyPointer.isActualProperty());
 assertFalse(nullPropertyPointer.isContainer());
 assertFalse(nullPropertyPointer.isLeaf());
 }

 @Test(expected = JXPathInvalidAccessException.class)
 public void testNullPropertyPointerSettingValue() {
 NodePointer parent = new NodePointer(new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setValue("test value");
 }

 @Test
 public void testNullPropertyPointerSettingPropertyName() {
 NodePointer parent = new NodePointer(new QName("root"));
 NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(parent);
 nullPropertyPointer.setPropertyName("testProperty");
 assertEquals("testProperty", nullPropertyPointer.getName().getLocalPart());
 }

 @Test
 public void testNullPropertyPointerCreatePath() {
 JXPathContext context = new JXPathContext(new Object());
 NodePointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
 NodePointer result = nullPropertyPointer.createPath(context);
 assertNotNull(result);
 assertFalse(result.isContainer());
 assertFalse(result.isLeaf());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPropertyPointerCreatePathWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 NodePointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
 nullPropertyPointer.createPath(context, "test value");
 }

 @Test
 public void testNullPropertyPointerCreateChild() {
 JXPathContext context = new JXPathContext(new Object());
 NodePointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
 NodePointer result = nullPropertyPointer.createChild(context, new QName("child"), 0);
 assertNotNull(result);
 assertFalse(result.isContainer());
 assertFalse(result.isLeaf());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPropertyPointerCreateChildWithValue() {
 JXPathContext context = new JXPathContext(new Object());
 NodePointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
 nullPropertyPointer.createChild(context, new QName("child"), 0, "test value");
 }

 @Test
 public void testNullPropertyPointerAsPath() {
 NodePointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
 assertEquals("root", nullPropertyPointer.asPath());
 }

 @Test
 public void testNullPropertyPointerAsPathWithNameAttribute() {
 NodePointer nullPropertyPointer = new NullPropertyPointer(new NodePointer(new QName("root")));
 nullPropertyPointer.setNameAttributeValue("testProperty");
 nullPropertyPointer.setIndex(1);
 assertEquals("[@name='testProperty'][1]", nullPropertyPointer.asPath());
 }
}
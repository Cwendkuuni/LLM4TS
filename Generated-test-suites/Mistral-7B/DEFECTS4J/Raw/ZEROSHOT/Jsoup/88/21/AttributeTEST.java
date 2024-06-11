import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AttributeTest {

 private Attribute attribute;

 @BeforeEach
 public void setUp() {
 attribute = new Attribute("testKey", "testValue");
 }

 @Test
 public void testAttributeCreation() {
 // Test creating a new attribute with raw key and value
 Attribute newAttribute = new Attribute("newKey", "newValue");

 // Test creating a new attribute with raw key, value and parent
 Attributes parentAttributes = new Attributes();
 Attribute newAttributeWithParent = new Attribute("newKeyWithParent", "newValueWithParent", parentAttributes);

 // Test creating an attribute from encoded key and value
 String encodedKey = "testKey=test";
 String encodedValue = "testValue=testEncoded";
 Attribute createdFromEncoded = Attribute.createFromEncoded("test", encodedValue);

 // Test setting key and value
 attribute.setKey("changedKey");
 attribute.setValue("changedValue");

 // Test getting key and value
 assertEquals("testKey", attribute.getKey());
 assertEquals("testValue", attribute.getValue());
 }

 @Test
 public void testSettingKey() {
 attribute.setKey("changedKey");
 assertEquals("changedKey", attribute.getKey());
 }

 @Test
 public void testSettingValue() {
 attribute.setValue("changedValue");
 assertEquals("testValue", attribute.getValue());
 assertEquals("changedValue", attribute.getValue());
 }

 @Test
 public void testHtmlMethod() throws IOException {
 StringBuilder sb = StringUtil.borrowBuilder();
 attribute.html(sb, new Document("").outputSettings());
 String html = StringUtil.releaseBuilder(sb);
 assertTrue(html.contains("testKey=\"testValue\""));
 }

 @Test
 public void testToStringMethod() {
 assertEquals(attribute.toString(), attribute.html());
 }

 @Test
 public void testIsDataAttributeMethod() {
 assertFalse(attribute.isDataAttribute());
 String dataAttributeKey = "data-test";
 Attribute dataAttribute = new Attribute(dataAttributeKey, null, null);
 assertTrue(dataAttribute.isDataAttribute());
 }

 @Test
 public void testShouldCollapseAttributeMethod() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 assertTrue(attribute.shouldCollapseAttribute(outputSettings));
 assertTrue(attribute.shouldCollapseAttribute(outputSettings, true));
 String booleanAttributeKey = "checked";
 Attribute booleanAttribute = new Attribute(booleanAttributeKey, null, null);
 assertFalse(booleanAttribute.shouldCollapseAttribute(outputSettings));
 assertFalse(booleanAttribute.shouldCollapseAttribute(outputSettings, true));
 }

 @Test
 public void testIsBooleanAttributeMethod() {
 assertFalse(attribute.isBooleanAttribute());
 assertTrue(Arrays.binarySearch(Attribute.booleanAttributes, "checked") >= 0);
 Attribute booleanAttribute = new Attribute("checked", null, null);
 assertTrue(booleanAttribute.isBooleanAttribute());
 }

 @Test
 public void testEqualsMethod() {
 Attribute sameAttribute = new Attribute("testKey", "testValue");
 Attribute differentAttribute = new Attribute("testKeyDifferent", "testValue");
 Attribute sameAttributeWithDifferentValue = new Attribute("testKey", "testValueDifferent");
 Attribute nullAttribute = null;
 Attribute sameAttributeCloned = attribute.clone();

 assertEquals(attribute, attribute);
 assertEquals(attribute, sameAttribute);
 assertEquals(sameAttribute, sameAttributeCloned);
 assertNotEquals(attribute, differentAttribute);
 assertNotEquals(attribute, nullAttribute);
 assertNotEquals(attribute, new Object());
 }

 @Test
 public void testClone() {
 Attribute clonedAttribute = attribute.clone();
 assertEquals(attribute, clonedAttribute);
 attribute.setKey("changedKey");
 assertNotEquals(attribute, clonedAttribute);
 }

 @AfterEach
 public void tearDown() {
 StringUtil.cleanUp();
 }

}
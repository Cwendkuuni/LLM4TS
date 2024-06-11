import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultParseSettings() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagNameLowerCased = htmlDefault.normalizeTag("HTML");
 String tagNamePreserved = htmlDefault.normalizeTag("HTML");
 String attributeNameLowerCased = htmlDefault.normalizeAttribute("id");
 String attributeNamePreserved = htmlDefault.normalizeAttribute("id");

 // Test normalizeTag method with lowerCase settings
 assertThat(tagNameLowerCased, is(equalTo("html")));
 // Test normalizeTag method with preserveCase settings
 assertThat(tagNamePreserved, is(equalTo("HTML")));
 // Test normalizeAttribute method with lowerCase settings
 assertThat(attributeNameLowerCased, is(equalTo("id")));
 // Test normalizeAttribute method with preserveCase settings
 assertThat(attributeNamePreserved, is(equalTo("id")));
 }

 @Test
 public void testPreserveCaseParseSettings() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagNameLowerCased = preserveCase.normalizeTag("DIV");
 String tagNamePreserved = preserveCase.normalizeTag("DIV");
 String attributeNameLowerCased = preserveCase.normalizeAttribute("CLASS");
 String attributeNamePreserved = preserveCase.normalizeAttribute("CLASS");

 // Test normalizeTag method with lowerCase settings
 assertThat(tagNameLowerCased, is(equalTo("div")));
 // Test normalizeTag method with preserveCase settings
 assertThat(tagNamePreserved, is(equalTo("DIV")));
 // Test normalizeAttribute method with lowerCase settings
 assertThat(attributeNameLowerCased, is(equalTo("class")));
 // Test normalizeAttribute method with preserveCase settings
 assertThat(attributeNamePreserved, is(equalTo("CLASS")));
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes attributes = new Attributes();
 attributes.put("id", "test123");
 attributes.put("class", "testClass");

 // Test normalizeAttributes method with lowerCase settings
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 Attributes normalizedAttributesLowerCased = htmlDefault.normalizeAttributes(attributes);
 String idLowerCasedAttributeName = normalizedAttributesLowerCased.get("id");
 String classLowerCasedAttributeName = normalizedAttributesLowerCased.get("class");

 // Test normalizeAttributes method with preserveCase settings
 Attributes normalizedAttributesPreserved = preserveCase.normalizeAttributes(attributes);
 String idPreservedAttributeName = normalizedAttributesPreserved.get("id");
 String classPreservedAttributeName = normalizedAttributesPreserved.get("class");

 // Test normalizeAttributes method with lowerCase settings
 assertThat(idLowerCasedAttributeName, is(equalTo("id")));
 assertThat(classLowerCasedAttributeName, is(equalTo("class")));

 // Test normalizeAttributes method with preserveCase settings
 assertThat(idPreservedAttributeName, is(equalTo("id")));
 assertThat(classPreservedAttributeName, is(equalTo("class")));
 }

 @Test
 public void testNullArguments() {
 ParseSettings preserveCase = ParseSettings.preserveCase;

 // Test normalizeTag method with null tag name
 String nullTagName = null;
 String tagNamePreserved = preserveCase.normalizeTag(nullTagName);

 // Test normalizeTag method returns a non-null value
 assertThat(tagNamePreserved, notNullValue());
 // Test normalizeTag method with null tag name
 // should throw an exception
 Assert.assertThrows(NullPointerException.class, () -> preserveCase.normalizeTag(nullTagName));

 // Test normalizeAttribute method with null attribute name
 String nullAttributeName = null;
 String attributeNamePreserved = preserveCase.normalizeAttribute(nullAttributeName);

 // Test normalizeAttribute method returns a non-null value
 assertThat(attributeNamePreserved, notNullValue());
 // Test normalizeAttribute method with null attribute name
 // should throw an exception
 Assert.assertThrows(NullPointerException.class, () -> preserveCase.normalizeAttribute(nullAttributeName));

 // Test normalizeAttributes method with null attributes
 Attributes nullAttributes = null;
 Attributes normalizedAttributes = preserveCase.normalizeAttributes(nullAttributes);

 // Test normalizeAttributes method returns a non-null value
 assertThat(normalizedAttributes, notNullValue());
 // Test normalizeAttributes method with null attributes
 // should throw an exception
 Assert.assertThrows(NullPointerException.class, () -> preserveCase.normalizeAttributes(nullAttributes));
 }
}
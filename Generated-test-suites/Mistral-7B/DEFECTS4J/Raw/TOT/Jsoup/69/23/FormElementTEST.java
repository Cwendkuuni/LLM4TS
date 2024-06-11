import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Elements;
import org.jsoup.parser.ParseSetup;

public class FormElementTest {

 @BeforeClass
 public static void setup() {
 ParseSetup.setDefaultParseErrorHandler(parseException -> {});
 }

 @Test
 public void testFormElementCreation() {
 Document doc = Jsoup.parse("<form action='/' method='get'><input type='text' name='username' /></form>");
 FormElement form = (FormElement) doc.selectFirst("form");

 Assert.assertNotNull(form);
 Assert.assertEquals(1, form.elements().size());
 }

 @Test
 public void testEmptyFormElement() {
 Document doc = Jsoup.parse("<form></form>");
 FormElement form = (FormElement) doc.selectFirst("form");

 Assert.assertNotNull(form);
 Assert.assertEquals(0, form.elements().size());
 }

 @Test
 public void testFormWithMultipleControls() {
 Document doc = Jsoup.parse("<form action='/' method='get'><input type='text' name='username' /><select name='country'><option value='USA'>USA</option><option value='Canada'>Canada</option></select></form>");
 FormElement form = (FormElement) doc.selectFirst("form");

 Assert.assertNotNull(form);
 Assert.assertEquals(2, form.elements().size());
 }

 @Test
 public void testDisabledControl() {
 Document doc = Jsoup.parse("<form action='/' method='get'><input type='text' name='username' disabled /></form>");
 FormElement form = (FormElement) doc.selectFirst("form");

 Assert.assertNotNull(form);
 Assert.assertEquals(1, form.elements().size());
 Assert.assertFalse(form.elements().get(0).hasAttr("disabled"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormWithoutActionAttribute() {
 Document doc = Jsoup.parse("<form method='get'></form>");
 FormElement form = (FormElement) doc.selectFirst("form");

 form.submit();
 }

 @Test
 public void testFormWithMultipleSelectElements() {
 Document doc = Jsoup.parse("<form action='/' method='get'><select name='colors'><option value='red'>Red</option><option value='green'>Green</option><option value='blue'>Blue</option></select></form>");
 FormElement form = (FormElement) doc.selectFirst("form");

 List<Connection.KeyVal> data = form.formData();
 Assert.assertEquals(1, data.size());
 Assert.assertEquals("colors", data.get(0).getKey());
 Assert.assertEquals("red", data.get(0).getValue());
 }

 @Test
 public void testFormWithMultipleCheckboxAndRadioElements() {
 Document doc = Jsoup.parse("<form action='/' method='get'><input type='checkbox' name='colors[]' value='red' /><input type='checkbox' name='colors[]' value='green' checked /><input type='radio' name='size' value='small' checked /><input type='radio' name='size' value='large' /></form>");
 FormElement form = (FormElement) doc.selectFirst("form");

 List<Connection.KeyVal> data = form.formData();
 Assert.assertEquals(3, data.size());
 Assert.assertEquals("colors", data.get(0).getKey());
 Assert.assertEquals("red", data.get(0).getValue());
 Assert.assertEquals("colors", data.get(1).getKey());
 Assert.assertEquals("green", data.get(1).getValue());
 Assert.assertEquals("size", data.get(2).getKey());
 Assert.assertEquals("large", data.get(2).getValue());
 }
}
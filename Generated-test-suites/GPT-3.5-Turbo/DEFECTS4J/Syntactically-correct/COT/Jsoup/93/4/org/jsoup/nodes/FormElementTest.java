
import org.junit.Test;
import org.junit.Assert;

public class FormElementTest {
    
    // Test elements method
    @Test
    public void testElementsMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = form.elements();
        
        Assert.assertNotNull(elements);
        Assert.assertEquals(0, elements.size());
        
        // Add some elements to the form
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes());
        form.addElement(element1);
        form.addElement(element2);
        
        elements = form.elements();
        
        Assert.assertEquals(2, elements.size());
        Assert.assertEquals(element1, elements.get(0));
        Assert.assertEquals(element2, elements.get(1));
    }
    
    // Test addElement method
    @Test
    public void testAddElementMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        form.addElement(element);
        
        Elements elements = form.elements();
        
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(element, elements.get(0));
    }
    
    // Test removeChild method
    @Test
    public void testRemoveChildMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        Element element2 = new Element(Tag.valueOf("textarea"), "http://example.com", new Attributes());
        form.addElement(element1);
        form.addElement(element2);
        
        Assert.assertEquals(2, form.elements().size());
        
        form.removeChild(element1);
        
        Assert.assertEquals(1, form.elements().size());
        Assert.assertEquals(element2, form.elements().get(0));
    }
    
    // Test submit method
    @Test
    public void testSubmitMethod() {
        FormElement form1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Connection connection1 = form1.submit();
        
        Assert.assertNotNull(connection1);
        Assert.assertEquals("http://example.com", connection1.request().url().toString());
        
        FormElement form2 = new FormElement(Tag.valueOf("form"), "", new Attributes());
        form2.attr("action", "/submit");
        Connection connection2 = form2.submit();
        
        Assert.assertEquals("/submit", connection2.request().url().toString());
    }
    
    // Test formData method
    @Test
    public void testFormDataMethod() {
        FormElement form = new FormElement(Tag.valueOf("form"), "", new Attributes());
        
        Element input = new Element(Tag.valueOf("input"), "", new Attributes());
        input.attr("name", "username");
        input.attr("type", "text");
        input.val("testuser");
        form.addElement(input);
        
        Element select = new Element(Tag.valueOf("select"), "", new Attributes());
        select.attr("name", "country");
        Element option1 = new Element(Tag.valueOf("option"), "", new Attributes());
        option1.attr("value", "usa");
        option1.attr("selected", "");
        select.appendChild(option1);
        Element option2 = new Element(Tag.valueOf("option"), "", new Attributes());
        option2.attr("value", "uk");
        select.appendChild(option2);
        form.addElement(select);
        
        List<Connection.KeyVal> formData = form.formData();
        
        Assert.assertEquals(2, formData.size());
        
        Connection.KeyVal usernameKeyVal = formData.get(0);
        Assert.assertEquals("username", usernameKeyVal.key());
        Assert.assertEquals("testuser", usernameKeyVal.value());
        
        Connection.KeyVal countryKeyVal = formData.get(1);
        Assert.assertEquals("country", countryKeyVal.key());
        Assert.assertEquals("usa", countryKeyVal.value());
    }
}


import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


public class FormElementTest {
    
    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://www.example.com";
        Attributes attributes = new Attributes();
        
        formElement = new FormElement(tag, baseUri, attributes);
        
        Element inputElement = new Element(Tag.valueOf("input"), baseUri, attributes);
        formElement.addElement(inputElement);
        
        Element selectElement = new Element(Tag.valueOf("select"), baseUri, attributes);
        selectElement.attr("option[selected]", "true");
        formElement.addElement(selectElement);
    }

    @Test
    public void testAddElement() {
        // Arrange
        Elements elementsBeforeAdd = formElement.elements();

        // Act
        Element newElement = new Element(Tag.valueOf("input"), formElement.baseUri(), new Attributes());
        formElement.addElement(newElement);
        Elements elementsAfterAdd = formElement.elements();

        // Assert
        Assert.assertEquals(elementsBeforeAdd.size() + 1, elementsAfterAdd.size());
        Assert.assertTrue(elementsAfterAdd.contains(newElement));
    }

    @Test
    public void testSubmit() {
        // Arrange
        String action = "http://www.example.com/submit";
        formElement.attr("action", action);

        // Act
        Connection connection = formElement.submit();

        // Assert
        Assert.assertNotNull(connection);
        Assert.assertEquals(connection.request().method(), Connection.Method.POST);
        Assert.assertEquals(connection.request().url().toString(), action);
        // Additional assertions can be added based on specific requirements
    }

    @Test
    public void testSubmitWithEmptyAction() {
        // Arrange
        formElement.removeAttr("action");

        // Act
        try {
            Connection connection = formElement.submit();
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            Assert.assertEquals(e.getMessage(), "Could not determine a form action URL for submit. Ensure you set a base URI when parsing.");
        }
    }

    @Test
    public void testFormData() {
        // Act
        List<Connection.KeyVal> formData = formElement.formData();

        // Assert
        Assert.assertEquals(formData.size(), 2);
        // Additional assertions can be added based on specific requirements
    }

    @Test
    public void testEquals() {
        // Arrange
        FormElement formElement2 = new FormElement(formElement.tag(), formElement.baseUri(), formElement.attributes());

        // Act
        boolean isEqual = formElement.equals(formElement2);

        // Assert
        Assert.assertTrue(isEqual);
    }

}
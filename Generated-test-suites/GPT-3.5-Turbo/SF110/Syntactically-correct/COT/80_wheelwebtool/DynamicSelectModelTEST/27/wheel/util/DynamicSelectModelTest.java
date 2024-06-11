
package wheel.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DynamicSelectModelTest {
	
	private DynamicSelectModel dynamicSelectModel;
	
	@Before
	public void setUp() {
		dynamicSelectModel = new DynamicSelectModel();
	}

	@Test
	public void testConstructorWithArguments() {
		// Arrange
		String collection = "collection";
		String label = "label";
		String value = "value";
		
		// Act
		dynamicSelectModel = new DynamicSelectModel(new ElExpression(collection), new ElExpression(label), new ElExpression(value));
		
		// Assert
		assertNotNull(dynamicSelectModel);
		assertEquals(collection, dynamicSelectModel.getCollection().toString());
		assertEquals(label, dynamicSelectModel.getLabel().toString());
		assertEquals(value, dynamicSelectModel.getValue().toString());
	}
	
	@Test
	public void testConstructorWithoutArguments() {
		// Act
		dynamicSelectModel = new DynamicSelectModel();
		
		// Assert
		assertNotNull(dynamicSelectModel);
		assertNull(dynamicSelectModel.getCollection());
		assertNull(dynamicSelectModel.getLabel());
		assertNull(dynamicSelectModel.getValue());
	}
	
	@Test
	public void testCollection() {
		// Arrange
		String collection = "collection";
		
		// Act
		dynamicSelectModel.collection(collection);
		
		// Assert
		assertEquals(collection, dynamicSelectModel.getCollection().toString());
	}
	
	@Test
	public void testLabel() {
		// Arrange
		String label = "label";
		
		// Act
		dynamicSelectModel.label(label);
		
		// Assert
		assertEquals(label, dynamicSelectModel.getLabel().toString());
	}
	
	@Test
	public void testValue() {
		// Arrange
		String value = "value";
		
		// Act
		dynamicSelectModel.value(value);
		
		// Assert
		assertEquals(value, dynamicSelectModel.getValue().toString());
	}
	
	@Test
	public void testEnumeration() {
		// Arrange
		String enumeration = "enumeration";
		ElExpression expectedCollection = new ElExpression(enumeration + ".values()");
		ElExpression expectedLabel = new ElExpression("toString()");
		ElExpression expectedValue = new ElExpression("ordinal()");
		
		// Act
		dynamicSelectModel.enumeration(enumeration);
		
		// Assert
		assertEquals(expectedCollection.toString(), dynamicSelectModel.getCollection().toString());
		assertEquals(expectedLabel.toString(), dynamicSelectModel.getLabel().toString());
		assertEquals(expectedValue.toString(), dynamicSelectModel.getValue().toString());
	}
	
	@Test
	public void testTranslator() {
		// Arrange
		String translator = "translator";
		
		// Act
		dynamicSelectModel.translator(translator);
		
		// Assert
		assertEquals(translator, dynamicSelectModel.getTranslator().toString());
	}
	
	@Test
	public void testEmpty() {
		// Arrange
		Object emptyObject = new Object();
		
		// Act
		dynamicSelectModel.empty(emptyObject);
		
		// Assert
		assertEquals(emptyObject, dynamicSelectModel.getEmptyObject());
	}
	
	@Test
	public void testGetOptionCount() {
		// Arrange
		dynamicSelectModel.setObjects(Arrays.asList("Option1", "Option2", "Option3"));
		int expectedOptionCount = 3;
		
		// Act
		int actualOptionCount = dynamicSelectModel.getOptionCount();
		
		// Assert
		assertEquals(expectedOptionCount, actualOptionCount);
	}
	
	@Test
	public void testGetLabel() {
		// Arrange
		int index = 0;
		dynamicSelectModel.setObjects(Arrays.asList("Option1", "Option2"));
		String expectedLabel = "Option1";
		
		// Act
		String actualLabel = dynamicSelectModel.getLabel(index);
		
		// Assert
		assertEquals(expectedLabel, actualLabel);
	}
	
	@Test
	public void testGetValue() {
		// Arrange
		int index = 0;
		dynamicSelectModel.setObjects(Arrays.asList("Value1", "Value2"));
		String expectedValue = "Value1";
		
		// Act
		String actualValue = dynamicSelectModel.getValue(index);
		
		// Assert
		assertEquals(expectedValue, actualValue);
	}
	
	@Test
	public void testTranslateValue() {
		// Arrange
		String value = "Value1";
		dynamicSelectModel.setObjects(Arrays.asList("Option1", "Option2"));
		String expectedTranslatedValue = "TranslatedValue1";
		
		// Act
		String actualTranslatedValue = (String) dynamicSelectModel.translateValue(value);
		
		// Assert
		assertEquals(expectedTranslatedValue, actualTranslatedValue);
	}
	
	@Test
	public void testGetObjects() {
		// Arrange
		List<Object> expectedObjects = Arrays.asList("Object1", "Object2");
		dynamicSelectModel.setObjects(expectedObjects);
		
		// Act
		Collection<Object> actualObjects = dynamicSelectModel.getObjects();
		
		// Assert
		assertEquals(expectedObjects, actualObjects);
	}
	
	@Test
	public void testGetComponent() {
		// Arrange
		Component component = new Component();
		dynamicSelectModel.setComponent(component);
		
		// Act
		Component actualComponent = dynamicSelectModel.getComponent();
		
		// Assert
		assertEquals(component, actualComponent);
	}
	
	@Test
	public void testSetComponent() {
		// Arrange
		Component component = new Component();
		
		// Act
		dynamicSelectModel.setComponent(component);
		
		// Assert
		assertEquals(component, dynamicSelectModel.getComponent());
	}
	
	@Test
	public void testGetTopLevelComponent() {
		// Arrange
		StandaloneComponent topLevelComponent = new StandaloneComponent();
		dynamicSelectModel.setTopLevelComponent(topLevelComponent);
		
		// Act
		StandaloneComponent actualTopLevelComponent = dynamicSelectModel.getTopLevelComponent();
		
		// Assert
		assertEquals(topLevelComponent, actualTopLevelComponent);
	}
	
	@Test
	public void testSetTopLevelComponent() {
		// Arrange
		StandaloneComponent topLevelComponent = new StandaloneComponent();
		
		// Act
		dynamicSelectModel.setTopLevelComponent(topLevelComponent);
		
		// Assert
		assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
	}
	
	@Test(expected = WheelException.class)
	public void testValidate_ThrowsWheelExceptionWhenCollectionIsNull() {
		// Arrange
		dynamicSelectModel.setLabels(new ElExpression("label"));
		dynamicSelectModel.setValues(new ElExpression("numericValue"));
		
		// Act
		dynamicSelectModel.validate();
	}
	
	@Test(expected = WheelException.class)
	public void testValidate_ThrowsWheelExceptionWhenLabelIsNull() {
		// Arrange
		dynamicSelectModel.setCollection(new ElExpression("collection"));
		dynamicSelectModel.setValues(new ElExpression("numericValue"));
		
		// Act
		dynamicSelectModel.validate();
	}
	
	@Test(expected = WheelException.class)
	public void testValidate_ThrowsWheelExceptionWhenValueIsNull() {
		// Arrange
		dynamicSelectModel.setCollection(new ElExpression("collection"));
		dynamicSelectModel.setLabels(new ElExpression("label"));
		
		// Act
		dynamicSelectModel.validate();
	}
	
	@Test
	public void testReset() {
		// Arrange
		dynamicSelectModel.setObjects(Arrays.asList("Option1", "Option2"));
		
		// Act
		dynamicSelectModel.reset();
		
		// Assert
		assertNull(dynamicSelectModel.getObjects());
	}

}


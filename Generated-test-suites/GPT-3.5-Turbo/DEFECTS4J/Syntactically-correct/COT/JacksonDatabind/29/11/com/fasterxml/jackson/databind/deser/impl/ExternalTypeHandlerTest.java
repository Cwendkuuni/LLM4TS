
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

	ExternalTypeHandler externalTypeHandler;
	HashMap<String, Integer> nameToPropertyIndex;
	ExternalTypeHandler.ExtTypedProperty[] properties;
	String[] typeIds;
	TokenBuffer[] tokens;
	
	@Before
	public void setUp() {
		properties = new ExternalTypeHandler.ExtTypedProperty[2];
		properties[0] = new ExternalTypeHandler.ExtTypedProperty(null, null);
		properties[1] = new ExternalTypeHandler.ExtTypedProperty(null, null);
		
		nameToPropertyIndex = new HashMap<String, Integer>();
		nameToPropertyIndex.put("property1", 0);
		nameToPropertyIndex.put("typeProperty1", 1);
		
		typeIds = new String[2];
		tokens = new TokenBuffer[2];
		externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
	}
	
	@Test
	public void testStart() {
		ExternalTypeHandler newHandler = externalTypeHandler.start();
		assertNotNull(newHandler);
		assertNotSame(externalTypeHandler, newHandler);
	}
	
	@Test
	public void testHandleTypePropertyValue() throws IOException {
		JsonParser jp = null;
		DeserializationContext ctxt = null;
		String propName = "typeProperty1";
		Object bean = new Object();
		
		boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
		assertFalse(result);
	}
	
	@Test
	public void testHandlePropertyValue() throws IOException {
		JsonParser jp = null;
		DeserializationContext ctxt = null;
		String propName = "property1";
		Object bean = new Object();
		
		boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
		assertFalse(result);
	}
	
	@Test
	public void testComplete() throws IOException {
		JsonParser p = null;
		DeserializationContext ctxt = null;
		Object bean = new Object();
		
		Object result = externalTypeHandler.complete(p, ctxt, bean);
		assertSame(bean, result);
	}
	
	@Test
	public void testCompleteWithCreator() throws IOException {
		JsonParser jp = null;
		DeserializationContext ctxt = null;
		PropertyValueBuffer buffer = null;
		PropertyBasedCreator creator = null;
		
		Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
		assertNull(result);
	}
	
	@Test(expected = NullPointerException.class)
	public void testDeserialize() throws IOException {
		JsonParser p = null;
		DeserializationContext ctxt = null;
		int index = 0;
		String typeId = "type1";
		
		Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);
		assertNull(result);
	}
	
	@Test
	public void testDeserializeAndSet() throws IOException {
		JsonParser p = null;
		DeserializationContext ctxt = null;
		Object bean = new Object();
		int index = 0;
		String typeId = "type1";
		
		externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
		
		// As the method is void, we can only assert that it doesn't throw any exception
	}

}

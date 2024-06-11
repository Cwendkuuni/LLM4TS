package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> serializer;

	@Before
	public void setUp() {
		serializer = new DateTimeSerializerBase<Date>(Date.class, true, new SimpleDateFormat("yyyy-MM-dd"));
	}
	
	@After
	public void tearDown() {
		serializer = null;
	}
    
	@Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> updatedSerializer = serializer.withFormat(true, new SimpleDateFormat("MM/dd/yyyy"));
        assertEquals(true, updatedSerializer._useTimestamp);
        assertEquals("MM/dd/yyyy", ((SimpleDateFormat)updatedSerializer._customFormat).toPattern());
    }
    
    @Test
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        assertEquals(serializer, result);
    }
    
    @Test
    public void testCreateContextualWithNonNullPropertyWithNumericShape() throws JsonMappingException {
        Annotated property = new Annotated() {
            
            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> clazz) {
                if (clazz.equals(JsonFormat.class)) {
                    return (A) new JsonFormat() {
                        
                        @Override
                        public Class<? extends java.lang.annotation.Annotation> annotationType() {
                            return JsonFormat.class;
                        }
                        
                        @Override
                        public JsonFormat.Shape shape() {
                            return JsonFormat.Shape.NUMBER;
                        }

						@Override
						public String pattern() {
							return null;
						}

						@Override
						public String timezone() {
							return null;
						}

						@Override
						public String locale() {
							return null;
						}

						@Override
						public JsonFormat.Feature[] without() {
							return null;
						}

						@Override
						public JsonFormat.Feature[] with() {
							return null;
						}

						@Override
						public boolean lenient() {
							return false;
						}
                    };
                }
                return null;
            }
            
            @Override
            public <A extends java.lang.annotation.Annotation> A[] getAnnotationsByType(Class<A> clazz) {
                return null;
            }
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        BeanProperty beanProperty = new BeanProperty.Std(null, Date.class, null, property);
        JsonSerializer<?> result = serializer.createContextual(serializers, beanProperty);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertEquals(true, ((DateTimeSerializerBase)result)._useTimestamp);
    }
    
    @Test
    public void testCreateContextualWithNonNullPropertyWithStringShape() throws JsonMappingException {
        Annotated property = new Annotated() {
            
            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> clazz) {
                if (clazz.equals(JsonFormat.class)) {
                    return (A) new JsonFormat() {
                        
                        @Override
                        public Class<? extends java.lang.annotation.Annotation> annotationType() {
                            return JsonFormat.class;
                        }
                        
                        @Override
                        public JsonFormat.Shape shape() {
                            return JsonFormat.Shape.STRING;
                        }

						@Override
						public String pattern() {
							return null;
						}

						@Override
						public String timezone() {
							return null;
						}

						@Override
						public String locale() {
							return null;
						}

						@Override
						public JsonFormat.Feature[] without() {
							return null;
						}

						@Override
						public JsonFormat.Feature[] with() {
							return null;
						}

						@Override
						public boolean lenient() {
							return false;
						}
                    };
                }
                return null;
            }
            
            @Override
            public <A extends java.lang.annotation.Annotation> A[] getAnnotationsByType(Class<A> clazz) {
                return null;
            }
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        BeanProperty beanProperty = new BeanProperty.Std(null, Date.class, null, property);
        JsonSerializer<?> result = serializer.createContextual(serializers, beanProperty);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertEquals(false, ((DateTimeSerializerBase)result)._useTimestamp);
        
        // Verify that the format is set correctly
        DateTimeSerializerBase<Date> dateTimeSerializerBase = (DateTimeSerializerBase<Date>) result;
        DateFormat customFormat = dateTimeSerializerBase._customFormat;
        assertNotNull(customFormat);
        assertEquals(StdDateFormat.DATE_FORMAT_STR_ISO8601, ((SimpleDateFormat) customFormat).toPattern());
    }
    
    @Test
    public void testGetSchemaWithTimestamp() {
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode objectNode = factory.objectNode();
        objectNode.put("type", "number");
        objectNode.put("format", "utc-millisec");
        JsonNode expectedValue = objectNode;
        assertEquals(expectedValue, serializer.getSchema(serializers, Date.class));
    }
    
    @Test
    public void testGetSchemaWithStringFormat() {
        Annotated property = new Annotated() {
            
            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> clazz) {
                if (clazz.equals(JsonFormat.class)) {
                    return (A) new JsonFormat() {
                        
                        @Override
                        public Class<? extends java.lang.annotation.Annotation> annotationType() {
                            return JsonFormat.class;
                        }
                        
                        @Override
                        public JsonFormat.Shape shape() {
                            return JsonFormat.Shape.STRING;
                        }
                        
                        @Override
                        public String pattern() {
                            return "MM/dd/yyyy";
                        }

						@Override
						public String timezone() {
							return null;
						}

						@Override
						public String locale() {
							return null;
						}

						@Override
						public JsonFormat.Feature[] without() {
							return null;
						}

						@Override
						public JsonFormat.Feature[] with() {
							return null;
						}

						@Override
						public boolean lenient() {
							return false;
						}
                    };
                }
                return null;
            }
            
            @Override
            public <A extends java.lang.annotation.Annotation> A[] getAnnotationsByType(Class<A> clazz) {
                return null;
            }
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        BeanProperty beanProperty = new BeanProperty.Std(null, Date.class, null, property);
        JsonSerializer<?> result = serializer.createContextual(serializers, beanProperty);
        
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode objectNode = factory.objectNode();
        objectNode.put("type", "string");
        objectNode.put("format", "date-time");
        JsonNode expectedValue = objectNode;
        assertEquals(expectedValue, result.getSchema(serializers, Date.class));
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithTimestamp() throws JsonMappingException {
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(serializers) {
            
            @Override
            public SerializerProvider getProvider() {
                return null;
            }
            
            @Override
            public void expectNumberFormat(Class<?> type) throws JsonMappingException {
                visitIntFormat(this, type, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
            }
            
            @Override
            public void expectStringFormat(Class<?> type) throws JsonMappingException {
                
            }
        };
        
        ObjectMapper mapper = new ObjectMapper();
        JavaType typeHint = mapper.constructType(Date.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithStringFormat() throws JsonMappingException {
        Annotated property = new Annotated() {
            
            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> clazz) {
                if (clazz.equals(JsonFormat.class)) {
                    return (A) new JsonFormat() {
                        
                        @Override
                        public Class<? extends java.lang.annotation.Annotation> annotationType() {
                            return JsonFormat.class;
                        }
                        
                        @Override
                        public JsonFormat.Shape shape() {
                            return JsonFormat.Shape.STRING;
                        }
                        
                        @Override
                        public String pattern() {
                            return "MM/dd/yyyy";
                        }

						@Override
						public String timezone() {
							return null;
						}

						@Override
						public String locale() {
							return null;
						}

						@Override
						public JsonFormat.Feature[] without() {
							return null;
						}

						@Override
						public JsonFormat.Feature[] with() {
							return null;
						}

						@Override
						public boolean lenient() {
							return false;
						}
                    };
                }
                return null;
            }
            
            @Override
            public <A extends java.lang.annotation.Annotation> A[] getAnnotationsByType(Class<A> clazz) {
                return null;
            }
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        BeanProperty beanProperty = new BeanProperty.Std(null, Date.class, null, property);
        JsonSerializer<?> result = serializer.createContextual(serializers, beanProperty);
        
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(serializers) {
            
            @Override
            public SerializerProvider getProvider() {
                return null;
            }
            
            @Override
            public void expectNumberFormat(Class<?> type) throws JsonMappingException {
                
            }
            
            @Override
            public void expectStringFormat(Class<?> type) throws JsonMappingException {
                visitStringFormat(this, type, JsonValueFormat.DATE_TIME);
            }
        };
        
        ObjectMapper mapper = new ObjectMapper();
        JavaType typeHint = mapper.constructType(Date.class);
        result.acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    @Test
    public void testSerializeWithNullValue() {
    	try {
    		serializer.serialize(null, null, null);
    	} catch (Exception e) {
    		fail("Should not throw an exception");
    	}
    }
    
    @Test
    public void testSerializeWithNonNullValue() {
    	try {
    		SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
    		JsonGenerator gen = null;
    		serializer.serialize(new Date(), gen, serializers);
    	} catch (Exception e) {
    		fail("Should not throw an exception");
    	}
    }

    @Test
    public void testIsEmptyWithNullValue() {
    	assertTrue(serializer.isEmpty((Date) null));
    }
    
    @Test
    public void testIsEmptyWithNonNullValue() {
    	Date date = new Date();
    	assertFalse(serializer.isEmpty(date));
    }
    
    @Test
    public void testIsEmptyWithSerializerProvider() {
    	SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
    	Date date = new Date();
    	assertFalse(serializer.isEmpty(serializers, date));
    }
    
    @Test
    public void testTimestamp() {
        Class<? extends DateTimeSerializerBase<Date>> clazz = serializer.getClass();
        Class<Date> type = Date.class;
        DateTimeSerializerBase<?> dateTimeSerializerBase = serializer;
        long result = dateTimeSerializerBase._timestamp(new Date());
        assertEquals(0, result);
    }
    
    @Test
    public void testVisitIntFormatWithJsonValueFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(null) {
            
            @Override
            public SerializerProvider getProvider() {
                return null;
            }
        };
        
        JavaType typeHint = null;
        JsonParser.NumberType numberType = JsonParser.NumberType.LONG;
        JsonValueFormat valueFormat = null;
        
        serializer.visitIntFormat(visitor, typeHint, numberType, valueFormat);
    }
    
    @Test
    public void testVisitStringFormatWithJsonValueFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(null) {
            
            @Override
            public SerializerProvider getProvider() {
                return null;
            }
        };
        
        JavaType typeHint = null;
        JsonValueFormat valueFormat = JsonValueFormat.DATE_TIME;
        
        serializer.visitStringFormat(visitor, typeHint, valueFormat);
    }

}
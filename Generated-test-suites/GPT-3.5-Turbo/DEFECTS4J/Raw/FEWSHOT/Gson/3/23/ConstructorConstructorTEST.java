package com.google.gson.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class ConstructorConstructorTest {
    // field variables for testing
    private Map<Class<?>, InstanceCreator<?>> instanceCreators;

    @Test
    public void testGet_withInstanceCreatorAndType() {
        // setup test case
        instanceCreators = new LinkedHashMap<>();
        instanceCreators.put(String.class, new InstanceCreator<String>() {
            @Override
            public String createInstance(Type type) {
                return "";
            }
        });
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        TypeToken<String> typeToken = new TypeToken<String>() {
            private static final long serialVersionUID = 1L;
        };

        // execute method
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);

        // assert result
        assertEquals("", objectConstructor.construct());
    }

    @Test
    public void testGet_withRawTypeCreator() {
        // setup test case
        instanceCreators = new LinkedHashMap<>();
        instanceCreators.put(Collection.class, new InstanceCreator<Collection<Object>>() {
            @Override
            public Collection<Object> createInstance(Type type) {
                return new ArrayList<>();
            }
        });
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        TypeToken<Collection<Object>> typeToken = new TypeToken<Collection<Object>>() {
            private static final long serialVersionUID = 1L;
        };

        // execute method
        ObjectConstructor<Collection<Object>> objectConstructor = constructorConstructor.get(typeToken);

        // assert result
        assertEquals(new ArrayList<>(), objectConstructor.construct());
    }

    @Test
    public void testGet_withDefaultConstructor() {
        // setup test case
        instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        TypeToken<String> typeToken = new TypeToken<String>() {
            private static final long serialVersionUID = 1L;
        };

        // execute method
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);

        // assert result
        assertEquals("", objectConstructor.construct());
    }

    @Test
    public void testGet_withDefaultImplementation() {
        // setup test case
        instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        TypeToken<Set<Object>> typeToken = new TypeToken<Set<Object>>() {
            private static final long serialVersionUID = 1L;
        };

        // execute method
        ObjectConstructor<Set<Object>> objectConstructor = constructorConstructor.get(typeToken);

        // assert result
        assertEquals(new LinkedHashSet<>(), objectConstructor.construct());
    }

    @Test
    public void testGet_withUnsafeConstructor() {
        // setup test case
        instanceCreators = new LinkedHashMap<>();
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        TypeToken<StringBuilder> typeToken = new TypeToken<StringBuilder>() {
            private static final long serialVersionUID = 1L;
        };

        // execute method
        ObjectConstructor<StringBuilder> objectConstructor = constructorConstructor.get(typeToken);

        // assert result
        assertEquals(new StringBuilder(), objectConstructor.construct());
    }

    // helper method for testing
    private ObjectConstructor<String> newDefaultConstructor(Class<? super String> rawType) {
        try {
            final Constructor<? super String> constructor = rawType.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return new ObjectConstructor<String>() {
                @SuppressWarnings("unchecked") // T is the same raw type as is requested
                @Override public String construct() {
                    try {
                        Object[] args = null;
                        return (String) constructor.newInstance(args);
                    } catch (InstantiationException e) {
                        // TODO: JsonParseException ?
                        throw new RuntimeException("Failed to invoke " + constructor + " with no args", e);
                    } catch (InvocationTargetException e) {
                        // TODO: don't wrap if cause is unchecked!
                        // TODO: JsonParseException ?
                        throw new RuntimeException("Failed to invoke " + constructor + " with no args",
                                e.getTargetException());
                    } catch (IllegalAccessException e) {
                        throw new AssertionError(e);
                    }
                }
            };
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked") // use runtime checks to guarantee that 'T' is what it is
    private <T> ObjectConstructor<T> newDefaultImplementationConstructor(
            final Type type, Class<? super T> rawType) {
        if (Collection.class.isAssignableFrom(rawType)) {
            if (SortedSet.class.isAssignableFrom(rawType)) {
                return new ObjectConstructor<T>() {
                    @Override public T construct() {
                        return (T) new TreeSet<Object>();
                    }
                };
            } else if (EnumSet.class.isAssignableFrom(rawType)) {
                return new ObjectConstructor<T>() {
                    @SuppressWarnings("rawtypes")
                    @Override public T construct() {
                        if (type instanceof ParameterizedType) {
                            Type elementType = ((ParameterizedType) type).getActualTypeArguments()[0];
                            if (elementType instanceof Class) {
                                return (T) EnumSet.noneOf((Class)elementType);
                            } else {
                                throw new JsonIOException("Invalid EnumSet type: " + type.toString());
                            }
                        } else {
                            throw new JsonIOException("Invalid EnumSet type: " + type.toString());
                        }
                    }
                };
            } else if (Set.class.isAssignableFrom(rawType)) {
                return new ObjectConstructor<T>() {
                    @Override public T construct() {
                        return (T) new LinkedHashSet<Object>();
                    }
                };
            } else if (Queue.class.isAssignableFrom(rawType)) {
                return new ObjectConstructor<T>() {
                    @Override public T construct() {
                        return (T) new LinkedList<Object>();
                    }
                };
            } else {
                return new ObjectConstructor<T>() {
                    @Override public T construct() {
                        return (T) new ArrayList<Object>();
                    }
                };
            }
        }

        if (Map.class.isAssignableFrom(rawType)) {
            if (SortedMap.class.isAssignableFrom(rawType)) {
                return new ObjectConstructor<T>() {
                    @Override public T construct() {
                        return (T) new TreeMap<Object, Object>();
                    }
                };
            } else if (type instanceof ParameterizedType && !(String.class.isAssignableFrom(
                    TypeToken.get(((ParameterizedType) type).getActualTypeArguments()[0]).getRawType()))) {
                return new ObjectConstructor<T>() {
                    @Override public T construct() {
                        return (T) new LinkedHashMap<Object, Object>();
                    }
                };
            } else {
                return new ObjectConstructor<T>() {
                    @Override public T construct() {
                        return (T) new LinkedTreeMap<String, Object>();
                    }
                };
            }
        }

        return null;
    }

    private <T> ObjectConstructor<T> newUnsafeAllocator(
            final Type type, final Class<? super T> rawType) {
        return new ObjectConstructor<T>() {
            private final UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
            @SuppressWarnings("unchecked")
            @Override public T construct() {
                try {
                    Object newInstance = unsafeAllocator.newInstance(rawType);
                    return (T) newInstance;
                } catch (Exception e) {
                    throw new RuntimeException(("Unable to invoke no-args constructor for " + type + ". "
                            + "Register an InstanceCreator with Gson for this type may fix this problem."), e);
                }
            }
        };
    }
}
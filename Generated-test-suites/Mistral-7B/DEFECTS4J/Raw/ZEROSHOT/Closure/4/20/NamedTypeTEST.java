import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyType;
import com.google.javascript.rhino.jstype.ProxyObjectType;
import com.google.javascript.rhino.jstype.StaticPropertyType;
import com.google.javascript.rhino.jstype.TypedefType;
import com.google.javascript.rhino.jstype.UnknownType;
import com.google.javascript.rhino.jstype.annotations.NamedType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class NamedTypeTest {

  private ErrorReporter errorReporter;
  private JSTypeRegistry registry;
  private StaticScope<JSType> staticScope;
  private NamedType namedType;

  @Before
  public void setup() {
    errorReporter = mock(ErrorReporter.class);
    registry = mock(JSTypeRegistry.class);
    staticScope = mock(StaticScope.class);
    when(registry.getType("String")).thenReturn(new StringType());
    when(registry.getType("Number")).thenReturn(new NumberType());
    when(registry.getType("Boolean")).thenReturn(new BooleanType());
    when(registry.getType("Function")).thenReturn(new FunctionType());
    when(registry.getType("Object")).thenReturn(new ObjectType());
    when(registry.getType("Enum")).thenReturn(new EnumType());
    when(registry.isLastGeneration()).thenReturn(true);
    when(registry.isForwardDeclaredType("String")).thenReturn(false);

    NamedType.setTypeRegistry(registry);
    namedType = new NamedType(registry, "String", "test.js", 1, 1);
  }

  @Test
  public void testIsNamedType() {
    assertThat(namedType.isNamedType()).isTrue();
  }

  @Test
  public void testIsNominalType() {
    assertThat(namedType.isNominalType()).isTrue();
  }

  @Test
  public void testGetReferenceName() {
    assertThat(namedType.getReferenceName()).isEqualTo("String");
  }

  @Test
  public void testToStringHelper() {
    assertThat(namedType.toStringHelper(false)).isEqualTo("String");
  }

  @Test
  public void testDefineProperty() {
    namedType.defineProperty("prop", new StringType(), false, null);
    ObjectType objType = Mockito.spy(new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)));
    namedType.finishPropertyContinuations();
    PropertyType prop = objType.getPropertyType("prop");
    assertThat(prop).isNotNull();
    assertThat(prop.getType()).isInstanceOf(StringType.class);
  }

  @Test
  public void testResolveInternalWithRegistryType() {
    when(staticScope.getSlot("String")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return new StringType();
      }
    });

    JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
    assertThat(resolvedType).isSameAs(new StringType());
  }

  @Test
  public void testResolveInternalWithProperties() {
    ObjectType objectType = Mockito.spy(new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)));
    when(staticScope.getSlot("Object")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return objectType;
      }
    });
    when(staticScope.getSlot("String")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return new StringType();
      }
    });

    when(objectType.getPropertyType("toString")).thenReturn(new PropertyType(
        new StringType(), new StaticPropertyType(false), null, null, null, null, null, null));

    JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
    assertThat(resolvedType).isSameAs(new StringType());
  }

  @Test
  public void testResolveInternalWithUnresolvedType() {
    when(staticScope.getSlot("Object")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE));
      }
    });

    when(staticScope.getSlot("String")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return null;
      }
    });

    namedType.resolveInternal(errorReporter, staticScope);
    assertThat(namedType.getReferencedType()).isInstanceOf(UnknownType.class);
  }

  @Test
  public void testResolveInternalWithFunctionType() {
    ObjectType objectType = Mockito.spy(new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)));
    when(staticScope.getSlot("Function")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return new FunctionType();
      }
    });
    when(staticScope.getSlot("String")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return null;
      }
    });

    when(objectType.getPropertyType("prototype")).thenReturn(new PropertyType(
        new FunctionType().getInstanceType(), new StaticPropertyType(false), null, null, null, null, null, null));

    JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
    assertThat(resolvedType).isSameAs(new FunctionType().getInstanceType());
  }

  @Test
  public void testResolveInternalWithEnumType() {
    ObjectType objectType = Mockito.spy(new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)));
    when(staticScope.getSlot("Enum")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return new EnumType();
      }
    });
    when(staticScope.getSlot("String")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return null;
      }
    });

    when(objectType.getPropertyType("elements")).thenReturn(new PropertyType(
        new EnumElementType(new StringType(), new StringType(), null, null, null, null, null, null, null),
        new StaticPropertyType(false), null, null, null, null, null, null));

    JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
    assertThat(resolvedType).isSameAs(new EnumElementType(new StringType(), new StringType(), null, null, null, null, null, null, null).getElementsType());
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof StringType;
      }
    };

    namedType.setValidator(validator);
    JSType referencedType = namedType.getReferencedType();
    assertThat(referencedType).isInstanceOf(UnknownType.class);
    namedType.resolveInternal(errorReporter, staticScope);
    JSType resolvedType = namedType.getReferencedType();
    assertThat(validator.apply(resolvedType)).isTrue();
  }

  @Test(expected = AssertionError.class)
  public void testResolveInternalWithCycle() {
    ObjectType cycleType = Mockito.spy(new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)));
    when(staticScope.getSlot("Object")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return cycleType;
      }
    });

    namedType.resolveInternal(errorReporter, staticScope);
  }

  @Test
  public void testResolveInternalWithEnumElementCycle() {
    ObjectType elementType = Mockito.spy(new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)));
    ObjectType enumType = Mockito.spy(new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE)));
    when(staticScope.getSlot("Object")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return enumType;
      }
    });
    when(enumType.getPropertyType("elements")).thenReturn(new PropertyType(
        new EnumElementType(elementType, new StringType(), null, null, null, null, null, null, null),
        new StaticPropertyType(false), null, null, null, null, null, null));
    when(elementType.getPropertyType("enumValue")).thenReturn(new PropertyType(
        namedType, new StaticPropertyType(false), null, null, null, null, null, null, null));

    namedType.resolveInternal(errorReporter, staticScope);
    JSType resolvedType = namedType.getReferencedType();
    assertThat(resolvedType).isSameAs(elementType);
  }

  @Test
  public void testResolveInternalWithUnresolvedTypeIgnoringForwardDeclarations() {
    when(staticScope.getSlot("Object")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE));
      }
    });

    when(staticScope.getSlot("ForwardDeclaredType")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return null;
      }
    });

    namedType = new NamedType(registry, "ForwardDeclaredType", "test.js", 1, 1);

    JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
    assertThat(resolvedType).isInstanceOf(UnknownType.class);
  }

  @Test
  public void testResolveInternalWithUnresolvedTypeWithValidator() {
    when(staticScope.getSlot("Object")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE));
      }
    });

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input == null;
      }
    };

    namedType = new NamedType(registry, "UnknownType", "test.js", 1, 1);
    namedType.setValidator(validator);

    JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
    assertThat(resolvedType).isSameAs(new UnknownType());
  }

  @Test
  public void testResolveInternalWithUnresolvedTypeWithValidatorAndForwardDeclaration() {
    when(staticScope.getSlot("Object")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return new ObjectType(registry, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE));
      }
    });

    when(staticScope.getSlot("ForwardDeclaredType")).thenReturn(new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return null;
      }
    });

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input == null;
      }
    };

    namedType = new NamedType(registry, "ForwardDeclaredType", "test.js", 1, 1);
    namedType.setValidator(validator);

    registry.setForwardDeclaredType("ForwardDeclaredType", new ObjectType(registry, new ObjectType(registry, new ObjectType(registry, new ObjectType(registry, new ObjectType(registry, new ObjectType(registry, new NativeObjectType(JSTypeNative.OBJECT))), "test.js", 1, 1)))));

    JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
    assertThat(resolvedType).isSameAs(new ObjectType(registry, new ObjectType(registry, new ObjectType(registry, new ObjectType(registry, new ObjectType(registry, new NativeObjectType(JSTypeNative.OBJECT)))))));
  }
}
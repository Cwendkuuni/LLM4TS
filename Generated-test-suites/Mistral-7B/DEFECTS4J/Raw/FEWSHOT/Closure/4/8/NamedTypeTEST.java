package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticallyTyped;
import com.google.javascript.rhino.Symbol;
import com.google.javascript.rhino.Type;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyDescriptor;

import java.util.List;

@StaticallyTyped
public class NamedTypeTest {

  private static final ErrorReporter errorReporter = new ErrorReporter();

  @Test
  public void testCreateNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      @Override
      public JSType getType(String name) {
        if ("StringType".equals(name)) {
          return StringType.getInstance();
        }
        return null;
      }
    };

    NamedType namedType = new NamedType(registry, "StringType", null, 0, 0);
    assertThat(namedType.getReferenceName()).isEqualTo("StringType");
    assertThat(namedType.getSourceName()).isNull();
  }

  @Test
  public void testNamedTypeResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      @Override
      public JSType getType(String name) {
        if ("StringType".equals(name)) {
          return StringType.getInstance();
        }
        return null;
      }
    };

    NamedType namedType = new NamedType(registry, "StringType", null, 0, 0);

    // Resolve via registry
    JSType resolvedType = namedType.resolveInternal(errorReporter, null);
    assertThat(resolvedType).isEqualTo(StringType.getInstance());
  }

  @Test
  public void testNamedTypeResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      @Override
      public JSType getType(String name) {
        return null;
      }
    };

    JSType registryType = new ObjectType(registry, "RegistryType") {
      @Override
      public JSType getReferencedType() {
        return StringType.getInstance();
      }
    };

    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.setSlot("RegistryType", registryType);

    NamedType namedType = new NamedType(registry, "RegistryType.StringType", null, 0, 0);

    // Resolve via properties
    JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
    assertThat(resolvedType).isEqualTo(StringType.getInstance());
  }

  @Test
  public void testNamedTypeResolveMultipleComponents() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      @Override
      public JSType getType(String name) {
        if ("RegistryType".equals(name)) {
          return new ObjectType(registry, "RegistryType") {
            @Override
            public JSType getReferencedType() {
              return new ObjectType(registry, "InnerRegistryType") {
                @Override
                public JSType getReferencedType() {
                  return StringType.getInstance();
                }
              };
            }
          };
        }
        return null;
      }
    };

    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.setSlot("RegistryType", registry.getType("RegistryType"));

    NamedType namedType = new NamedType(registry, "RegistryType.InnerRegistryType.StringType", null, 0, 0);

    // Resolve via properties
    JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);
    assertThat(resolvedType).isEqualTo(StringType.getInstance());
  }

  @Test
  public void testNamedTypePropertyContinuation() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      @Override
      public JSType getType(String name) {
        return null;
      }
    };

    StaticScope<JSType> staticScope = new StaticScope<>();
    NamedType namedType = new NamedType(registry, "NamedType", null, 0, 0);

    namedType.defineProperty("prop", IntegerType.getInstance(), false, null);

    PropertyContinuation continuation = new PropertyContinuation(
        "prop", IntegerType.getInstance(), false, null);
    namedType.setPropertyContinuation(continuation);

    namedType.finishPropertyContinuations();

    ObjectType resolvedType = (ObjectType) namedType.getReferencedType();
    PropertyDescriptor propertyDescriptor = resolvedType.getOwnPropertyDescriptor("prop");

    assertThat(propertyDescriptor.getValue()).isEqualTo(IntegerType.getInstance());
  }

  @Test
  public void testNamedTypeEmptyReferenceName() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      @Override
      public JSType getType(String name) {
        return null;
      }
    };

    NamedType namedType = new NamedType(registry, "", null, 0, 0);

    // Resolve should fail
    JSType resolvedType = namedType.resolveInternal(errorReporter, null);
    assertNull(resolvedType);
  }
}
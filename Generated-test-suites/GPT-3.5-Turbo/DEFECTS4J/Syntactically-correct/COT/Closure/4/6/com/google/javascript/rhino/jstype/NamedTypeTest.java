import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NamedTypeTest {
    private JSTypeRegistry registry = new JSTypeRegistry();
    private ErrorReporter errorReporter = new ErrorReporter() {
        @Override
        public void error(String s, String s1, int i, int i1) {
            Assert.fail(s);
        }

        @Override
        public void warning(String s, String s1, int i, int i1) {
            Assert.fail(s);
        }

        @Override
        public void error(String s) {
            Assert.fail(s);
        }

        @Override
        public void warning(String s) {
            Assert.fail(s);
        }
    };

    @Test
    public void testResolveViaProperties() {
        StaticScope<JSType> enclosing = new StaticScope<>(null);
        NamedType namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.NamedType", "", 0, 0);

        ErrorRecorderPredicate<JSType> validator = new ErrorRecorderPredicate<>();
        namedType.setValidator(validator);

        ObjectType objectType = new ObjectType(registry, JSTypeNative.TOP_OBJECT.getInstanceType());

        StaticScope<JSType> staticScope = new StaticScope<>(objectType);
        staticScope.addSlot("com", new StaticScope.SimpleSlot<>(registry.getNativeObjectType(JSTypeNative.TOP_OBJECT), true));
        staticScope.addSlot("com.google", new StaticScope.SimpleSlot<>(registry.getNativeObjectType(JSTypeNative.TOP_OBJECT), true));
        staticScope.addSlot("com.google.javascript", new StaticScope.SimpleSlot<>(registry.getNativeObjectType(JSTypeNative.TOP_OBJECT), true));
        staticScope.addSlot("com.google.javascript.rhino", new StaticScope.SimpleSlot<>(registry.getNativeObjectType(JSTypeNative.TOP_OBJECT), true));
        staticScope.addSlot("com.google.javascript.rhino.jstype", new StaticScope.SimpleSlot<>(objectType, true));

        JSType resolvedType = namedType.resolveInternal(errorReporter, staticScope);

        Assert.assertEquals(true, resolvedType.isObjectType());
        Assert.assertEquals(true, resolvedType.isNominalType());
        Assert.assertEquals(true, validatedTypes.contains(resolvedType));
    }

    @Test
    public void testResolveViaRegistry() {
        NamedType namedType = new NamedType(registry, "java.lang.String", "", 0, 0);
        JSType resolvedType = namedType.resolveInternal(errorReporter, new StaticScope<>(null));
        Assert.assertEquals(true, resolvedType.isString());
    }

    private static class ErrorRecorderPredicate<T> implements Predicate<T> {
        private final List<T> errorList = new ArrayList<T>();

        @Override
        public boolean apply(T t) {
            errorList.add(t);
            return true;
        }
    }
}
import java.util.*;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DeserializerFactory df = new DeserializerFactory();
        DeserializerCache cache = new DeserializerCache();
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(df);

        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        ReadableObjectId result = context.findObjectId(id, gen, resolverType);

        assert result != null;
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DeserializerFactory df = new DeserializerFactory();
        DeserializerCache cache = new DeserializerCache();
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(df);

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        DeserializerFactory df = new DeserializerFactory();
        DeserializerCache cache = new DeserializerCache();
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(df);

        Annotated ann = new Annotated();
        Object deserDef = new Object();

        JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);

        assert result != null;
    }

    @Test
    public void testKeyDeserializerInstance() {
        DeserializerFactory df = new DeserializerFactory();
        DeserializerCache cache = new DeserializerCache();
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(df);

        Annotated ann = new Annotated();
        Object deserDef = new Object();

        KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);

        assert result != null;
    }

    @Test
    public void testWith() {
        DeserializerFactory df = new DeserializerFactory();
        DeserializerCache cache = new DeserializerCache();
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(df);

        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext result = context.with(factory);

        assert result != null;
    }

    @Test
    public void testCreateInstance() {
        DeserializerFactory df = new DeserializerFactory();
        DeserializerCache cache = new DeserializerCache();
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(df);

        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = new JsonParser();
        InjectableValues values = new InjectableValues();

        DefaultDeserializationContext result = context.createInstance(config, jp, values);

        assert result != null;
    }
}
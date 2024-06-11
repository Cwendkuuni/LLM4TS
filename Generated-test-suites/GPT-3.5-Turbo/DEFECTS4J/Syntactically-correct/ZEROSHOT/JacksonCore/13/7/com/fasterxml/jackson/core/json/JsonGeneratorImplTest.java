package com.fasterxml.jackson.core.json;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.VersionUtil;

public class JsonGeneratorImplTest {

    @Mock
    private IOContext _ioContext;

    @InjectMocks
    private JsonGeneratorImpl jsonGeneratorImpl;

    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        JsonGeneratorImpl jsonGeneratorImpl = new JsonGeneratorImpl(_ioContext, 0, null);
        Assert.assertNotNull(jsonGeneratorImpl);
    }

    @Test
    public void testVersion() {
        VersionUtil.versionFor(JsonGeneratorImpl.class);
    }

    @Test
    public void testWriteStringField() throws IOException {
        jsonGeneratorImpl.writeStringField("test", "test value");
    }
}
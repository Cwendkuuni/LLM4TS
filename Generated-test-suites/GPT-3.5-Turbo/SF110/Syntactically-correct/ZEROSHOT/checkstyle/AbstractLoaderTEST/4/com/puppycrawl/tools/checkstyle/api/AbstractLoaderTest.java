package com.puppycrawl.tools.checkstyle.api;

import org.xml.sax.helpers.*;
import java.util.*;
import com.google.common.collect.*;
import javax.xml.parsers.*;
import java.io.*;
import org.xml.sax.*;

public abstract class AbstractLoader extends DefaultHandler
{
    private final Map<String, String> publicIdToResourceNameMap;
    private final XMLReader parser;

    protected AbstractLoader(final String publicId, final String dtdResourceName) throws SAXException, ParserConfigurationException {
        this(new HashMap<String, String>(1));
        this.publicIdToResourceNameMap.put(publicId, dtdResourceName);
    }

    protected AbstractLoader(final Map<String, String> publicIdToResourceNameMap) throws SAXException, ParserConfigurationException {
        this.publicIdToResourceNameMap = (Map<String, String>)Maps.newHashMap((Map<?, ?>)publicIdToResourceNameMap);
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        (this.parser = factory.newSAXParser().getXMLReader()).setContentHandler(this);
        this.parser.setEntityResolver(this);
        this.parser.setErrorHandler(this);
    }

    public void parseInputSource(final InputSource inputSource) throws IOException, SAXException {
        this.parser.parse(inputSource);
    }

    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
        if (this.publicIdToResourceNameMap.keySet().contains(publicId)) {
            final String dtdResourceName = this.publicIdToResourceNameMap.get(publicId);
            final ClassLoader loader = this.getClass().getClassLoader();
            final InputStream dtdIs = loader.getResourceAsStream(dtdResourceName);
            return new InputSource(dtdIs);
        }
        return super.resolveEntity(publicId, systemId);
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
        throw exception;
    }
}
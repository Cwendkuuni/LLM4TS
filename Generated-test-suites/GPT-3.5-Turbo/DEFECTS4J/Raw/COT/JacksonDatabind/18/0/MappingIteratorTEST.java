package com.fasterxml.jackson.databind;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;

/**
 * Iterator exposed by {@link ObjectMapper} when binding sequence of
 * objects. Extension is done to allow more convenient exposing of
 * {@link IOException} (which basic {@link Iterator} does not expose)
 */
public class MappingIterator<T> implements Iterator<T>, Closeable
{
    // All the code for the class

}
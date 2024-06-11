package org.junit.platform.commons.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

/**
 * Marker interface for a test method that should be executed in a specific order
 */
@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
public @interface Ordered {

    /**
     * The order of execution
     */
    int value();
}
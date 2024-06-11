package geo.google.mapping;

import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

        GeoAddress addr = new GeoAddress();
        GeoUsAddress result = functor.execute(addr);

        assertNotNull("Result should not be null", result);
    }
}
package geo.google.mapping;

import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import geo.google.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        // Typical use case
        GeoAddress input = new GeoAddress();
        // Set up input address details
        // Set up expected GeoUsAddress
        GeoUsAddress expected = new GeoUsAddress();
        expected.setCountry("United States");
        expected.setState("California");
        expected.setCounty("Santa Clara");
        expected.setCity("Cupertino");
        expected.setPostalCode("95014");
        expected.setAddressLine1("1 Infinite Loop");
        expected.setAddressLine2("Apple Campus");
        // Set up expected coordinate
        // Set up expected accuracy
        
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoUsAddress result = functor.execute(input);
        
        // Compare expected and actual GeoUsAddress objects
        assertEquals(expected, result);
    }

}
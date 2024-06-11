package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

import org.junit.Test;

public class AddressToUsAddressFunctorTest {
    @Test
    public void testExecute() {
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());

        assertEquals(null, AddressToUsAddressFunctor.getInstance().execute(addr));

        final GeoUsAddress ret = new GeoUsAddress();
        final AddressDetails.Country country = new AddressDetails.Country();
        addr.getAddressDetails().setCountry(country);

        assertEquals(null, AddressToUsAddressFunctor.getInstance().execute(addr));

        final List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        country.setCountryNameCode(countryNames);

        assertEquals(null, AddressToUsAddressFunctor.getInstance().execute(addr));

        final AddressDetails.Country.CountryNameCode countryCode = new AddressDetails.Country.CountryNameCode();
        countryNames.add(countryCode);

        assertEquals(null, AddressToUsAddressFunctor.getInstance().execute(addr));
        assertEquals("", ret.getCountry());

        countryCode.setContent("US");

        assertEquals(ret, AddressToUsAddressFunctor.getInstance().execute(addr));
        assertEquals("US", ret.getCountry());
    }
}
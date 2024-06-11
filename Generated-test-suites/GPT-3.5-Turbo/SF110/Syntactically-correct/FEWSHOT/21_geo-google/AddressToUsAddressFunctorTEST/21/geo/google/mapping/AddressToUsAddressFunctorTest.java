package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import geo.google.datamodel.*;
import geo.google.*;
import org.junit.Test;
import java.util.*;
import org.apache.commons.collections.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        // Create input GeoAddress object
        final GeoAddress addr = new GeoAddress();
        final AddressDetails addressDetails = new AddressDetails();
        final AddressDetails.Country country = new AddressDetails.Country();
        final List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        final AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        countryNames.add(countryNameCode);
        country.setCountryNameCode(countryNames);
        final AdministrativeArea adminArea = new AdministrativeArea();
        final List<AdministrativeArea.AdministrativeAreaName> names = new ArrayList<>();
        final AdministrativeArea.AdministrativeAreaName areaName = new AdministrativeArea.AdministrativeAreaName();
        areaName.setContent("California");
        names.add(areaName);
        adminArea.setAdministrativeAreaName(names);
        country.setAdministrativeArea(adminArea);
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);

        // Create expected GeoUsAddress object
        final GeoUsAddress expected = new GeoUsAddress();
        expected.setCountry("USA");
        expected.setState("California");

        // Execute the functor
        final AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        final GeoUsAddress result = functor.execute(addr);

        // Assert the result
        assertNotNull(result);
        assertEquals(expected.getCountry(), result.getCountry());
        assertEquals(expected.getState(), result.getState());
    }
    
}
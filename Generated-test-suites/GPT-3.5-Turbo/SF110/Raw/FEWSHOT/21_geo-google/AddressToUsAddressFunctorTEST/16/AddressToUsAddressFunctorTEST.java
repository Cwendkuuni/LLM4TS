package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.cli.Util;
import org.junit.Test;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.Locality;
import geo.google.datamodel.PostalCode;
import geo.google.datamodel.ThoroughfareNameType;
import oasis.names.tc.ciq.xsdschema.xal._2.*;

import java.util.ArrayList;
import java.util.List;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        GeoUsAddress result = functor.execute(addr);

        assertNotNull(result);

        // Test setting country
        AddressDetails.Country country = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryName = new AddressDetails.Country.CountryNameCode();
        countryName.setContent("USA");
        countryNames.add(countryName);
        country.setCountryNameCode(countryNames);
        addr.getAddressDetails().setCountry(country);
        result = functor.execute(addr);
        assertEquals("USA", result.getCountry());

        // Test setting state
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> names = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        names.add(adminAreaName);
        adminArea.setAdministrativeAreaName(names);
        country.setAdministrativeArea(adminArea);
        result = functor.execute(addr);
        assertEquals("California", result.getState());

        // Test setting county
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames = new ArrayList<>();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdminAreaName.setContent("Los Angeles");
        subNames.add(subAdminAreaName);
        subAdminArea.setSubAdministrativeAreaName(subNames);
        adminArea.setSubAdministrativeArea(subAdminArea);
        result = functor.execute(addr);
        assertEquals("Los Angeles", result.getCounty());

        // Test setting city
        Locality locality = new Locality();
        List<Locality.LocalityName> localNames = new ArrayList<>();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("Los Angeles");
        localNames.add(localityName);
        locality.setLocalityName(localNames);
        adminArea.setLocality(locality);
        result = functor.execute(addr);
        assertEquals("Los Angeles", result.getCity());

        // Test setting postal code
        PostalCode postalCode = new PostalCode();
        List<PostalCode.PostalCodeNumber> numbers = new ArrayList<>();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("90001");
        numbers.add(postalCodeNumber);
        locality.setPostalCode(postalCode);
        postalCode.setPostalCodeNumber(numbers);
        result = functor.execute(addr);
        assertEquals("90001", result.getPostalCode());

        // Test setting address lines
        ThoroughfareNameType thoroughfare1 = new ThoroughfareNameType();
        thoroughfare1.setContent("123 Main St");
        ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
        thoroughfare2.setContent("Apt 456");
        List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        thoroughfares.add(thoroughfare1);
        thoroughfares.add(thoroughfare2);
        locality.getThoroughfare().setThoroughfareName(thoroughfares);
        result = functor.execute(addr);
        assertEquals("123 Main St", result.getAddressLine1());
        assertEquals("Apt 456", result.getAddressLine2());

        // Test setting coordinate
        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(37.7749);
        coordinate.setLongitude(-122.4194);
        addr.setCoordinate(coordinate);
        result = functor.execute(addr);
        assertNotNull(result.getCoordinate());

        // Test setting accuracy
        addr.setAccuracy(0.5);
        result = functor.execute(addr);
        assertEquals(0.5, result.getAccuracy(), 0.001);
    }
}
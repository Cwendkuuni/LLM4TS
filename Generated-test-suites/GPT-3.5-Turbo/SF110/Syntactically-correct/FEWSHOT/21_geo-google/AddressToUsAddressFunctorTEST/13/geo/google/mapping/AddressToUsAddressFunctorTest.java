package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {
    
    GeoAddress geoAddress;
    
    @Before
    public void setUp() {
        geoAddress = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        geoAddress.setAddressDetails(addressDetails);
        GeoUsAddress geoUsAddress = new GeoUsAddress();
    }
    
    @Test
    public void testExecute() {
        GeoUsAddress expected = new GeoUsAddress();
        
        // Test case 1
        AddressDetails.Country country1 = new AddressDetails.Country();
        geoAddress.getAddressDetails().setCountry(country1);
        List<AddressDetails.Country.CountryNameCode> countryNames1 = new ArrayList<AddressDetails.Country.CountryNameCode>();
        AddressDetails.Country.CountryNameCode countryName1 = new AddressDetails.Country.CountryNameCode();
        countryName1.setContent("US");
        countryNames1.add(countryName1);
        country1.setCountryNameCode(countryNames1);
        expected.setCountry("US");
        
        GeoUsAddress actual = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertEquals(expected, actual);
        
        // Test case 2
        AddressDetails.Country country2 = new AddressDetails.Country();
        geoAddress.getAddressDetails().setCountry(country2);
        List<AddressDetails.Country.CountryNameCode> countryNames2 = new ArrayList<AddressDetails.Country.CountryNameCode>();
        AddressDetails.Country.CountryNameCode countryName2 = new AddressDetails.Country.CountryNameCode();
        countryName2.setContent("Canada");
        countryNames2.add(countryName2);
        country2.setCountryNameCode(countryNames2);
        
        actual = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertNull(actual);
        
        // Test case 3
        AdministrativeArea adminArea = new AdministrativeArea();
        geoAddress.getAddressDetails().setCountry(country1);
        country1.setAdministrativeArea(adminArea);
        List<AdministrativeArea.AdministrativeAreaName> names = new ArrayList<AdministrativeArea.AdministrativeAreaName>();
        AdministrativeArea.AdministrativeAreaName name = new AdministrativeArea.AdministrativeAreaName();
        name.setContent("California");
        names.add(name);
        adminArea.setAdministrativeAreaName(names);
        expected.setState("California");
        
        actual = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertEquals(expected, actual);
        
        // Test case 4
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames = new ArrayList<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName>();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subName.setContent("Los Angeles County");
        subNames.add(subName);
        subAdminArea.setSubAdministrativeAreaName(subNames);
        expected.setCounty("Los Angeles County");
        
        actual = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertEquals(expected, actual);
        
        // Test case 5
        Locality locality = new Locality();
        adminArea.setLocality(locality);
        List<Locality.LocalityName> localNames = new ArrayList<Locality.LocalityName>();
        Locality.LocalityName localName = new Locality.LocalityName();
        localName.setContent("Los Angeles");
        localNames.add(localName);
        locality.setLocalityName(localNames);
        PostalCode postalCode = new PostalCode();
        locality.setPostalCode(postalCode);
        List<PostalCode.PostalCodeNumber> numbers = new ArrayList<PostalCode.PostalCodeNumber>();
        PostalCode.PostalCodeNumber number = new PostalCode.PostalCodeNumber();
        number.setContent("90001");
        numbers.add(number);
        postalCode.setPostalCodeNumber(numbers);
        Thoroughfare thoroughfare = new Thoroughfare();
        thoroughfare.setThoroughfareName(new ThoroughfareNameType());
        List<ThoroughfareNameType> thoroughfares = new ArrayList<ThoroughfareNameType>();
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("Main St");
        thoroughfares.add(thoroughfareName);
        thoroughfare.setThoroughfareName(thoroughfares);
        locality.setThoroughfare(thoroughfare);
        expected.setCity("Los Angeles");
        expected.setPostalCode("90001");
        expected.setAddressLine1("Main St");
        
        actual = AddressToUsAddressFunctor.getInstance().execute(geoAddress);
        assertEquals(expected, actual);
    }

}
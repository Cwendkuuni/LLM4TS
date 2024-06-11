package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        // Setup
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        addr.setAddressDetails(addressDetails);
        AddressDetails.Country country = new AddressDetails.Country();
        addressDetails.setCountry(country);
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        countryNames.add(countryNameCode);
        country.setCountryNameCode(countryNames);
        AdministrativeArea adminArea = new AdministrativeArea();
        country.setAdministrativeArea(adminArea);
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminAreaNames.add(adminAreaName);
        adminArea.setAdministrativeAreaName(adminAreaNames);
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        adminArea.setSubAdministrativeArea(subAdminArea);
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subAdminAreaNames = new ArrayList<>();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdminAreaName.setContent("County 1");
        subAdminAreaNames.add(subAdminAreaName);
        subAdminArea.setSubAdministrativeAreaName(subAdminAreaNames);
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("City 1");
        List<Locality.LocalityName> localityNames = new ArrayList<>();
        localityNames.add(localityName);
        locality.setLocalityName(localityNames);
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("12345");
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
        postalCodeNumbers.add(postalCodeNumber);
        postalCode.setPostalCodeNumber(postalCodeNumbers);
        locality.setPostalCode(postalCode);
        Thoroughfare thoroughfare = new Thoroughfare();
        ThoroughfareNameType thoroughfareNameType1 = new ThoroughfareNameType();
        ThoroughfareNameType thoroughfareNameType2 = new ThoroughfareNameType();
        thoroughfareNameType1.setContent("Street 1");
        thoroughfareNameType2.setContent("Street 2");
        List<ThoroughfareNameType> thoroughfareNameTypes = new ArrayList<>();
        thoroughfareNameTypes.add(thoroughfareNameType1);
        thoroughfareNameTypes.add(thoroughfareNameType2);
        thoroughfare.setThoroughfareName(thoroughfareNameTypes);
        locality.setThoroughfare(thoroughfare);
        subAdminArea.setLocality(locality);
        addr.setCoordinate(new Coordinate());
        addr.setAccuracy(1.0);
        
        // Execute
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(addr);
        
        // Verify
        assertEquals("USA", result.getCountry());
        assertEquals("California", result.getState());
        assertEquals("County 1", result.getCounty());
        assertEquals("City 1", result.getCity());
        assertEquals("12345", result.getPostalCode());
        assertEquals("Street 1", result.getAddressLine1());
        assertEquals("Street 2", result.getAddressLine2());
        assertTrue(addr.getCoordinate().equals(result.getCoordinate()));
        assertEquals(1.0, result.getAccuracy(), 0.001);
    }
}
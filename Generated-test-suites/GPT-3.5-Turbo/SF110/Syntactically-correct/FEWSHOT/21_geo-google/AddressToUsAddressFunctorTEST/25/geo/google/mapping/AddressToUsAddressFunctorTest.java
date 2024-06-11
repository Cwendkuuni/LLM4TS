package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void testExecute() {
        GeoAddress addr = new GeoAddress();
        GeoUsAddress ret = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertNull(ret.getCountry());
        assertNull(ret.getState());
        assertNull(ret.getCounty());
        assertNull(ret.getCity());
        assertNull(ret.getPostalCode());
        assertNull(ret.getAddressLine1());
        assertNull(ret.getAddressLine2());
        assertNull(ret.getCoordinate());
        assertEquals(0, ret.getAccuracy());
        
        addr = createGeoAddress();
        ret = AddressToUsAddressFunctor.getInstance().execute(addr);
        assertEquals("United States", ret.getCountry());
        assertEquals("California", ret.getState());
        assertEquals("Los Angeles County", ret.getCounty());
        assertEquals("Los Angeles", ret.getCity());
        assertEquals("90001", ret.getPostalCode());
        assertEquals("123 Main St", ret.getAddressLine1());
        assertEquals("Apt 456", ret.getAddressLine2());
        // Add assertion for coordinate and accuracy if available
    }
    
    private GeoAddress createGeoAddress() {
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        addr.setAddressDetails(addressDetails);
        
        AddressDetails.Country country = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryName = new AddressDetails.Country.CountryNameCode();
        countryName.setContent("United States");
        countryNames.add(countryName);
        country.setCountryNameCode(countryNames);
        addressDetails.setCountry(country);
        
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminAreaNames.add(adminAreaName);
        adminArea.setAdministrativeAreaName(adminAreaNames);
        country.setAdministrativeArea(adminArea);
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subAdminAreaNames = new ArrayList<>();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdminAreaName.setContent("Los Angeles County");
        subAdminAreaNames.add(subAdminAreaName);
        subAdminArea.setSubAdministrativeAreaName(subAdminAreaNames);
        adminArea.setSubAdministrativeArea(subAdminArea);
        
        Locality locality = new Locality();
        List<Locality.LocalityName> localNames = new ArrayList<>();
        Locality.LocalityName localName = new Locality.LocalityName();
        localName.setContent("Los Angeles");
        localNames.add(localName);
        PostalCode postalCode = new PostalCode();
        List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("90001");
        postalCodeNumbers.add(postalCodeNumber);
        postalCode.setPostalCodeNumber(postalCodeNumbers);
        locality.setPostalCode(postalCode);
        locality.setLocalityName(localNames);
        adminArea.setLocality(locality);
        
        Thoroughfare thoroughfare = new Thoroughfare();
        List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("123 Main St");
        thoroughfares.add(thoroughfareName);
        thoroughfare.setThoroughfareName(thoroughfares);
        locality.setThoroughfare(thoroughfare);
        
        thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("Apt 456");
        thoroughfares.add(thoroughfareName);
        
        return addr;
    }
}